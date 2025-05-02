/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repository;

import java.util.ArrayList;
import java.util.List;
import models.Identifiable;
import services.FileManager;

/**
 *
 * @author Chan Yong Liang
 * @param <T>
 */
public abstract class BaseRepository<T extends Identifiable> {

    protected final FileManager fm = new FileManager();
    protected final String fileName;
    protected final String rowFormat;

    protected BaseRepository(String fileName, String rowFormat) {
        this.fileName = fileName;
        this.rowFormat = rowFormat;
    }

    public void save(T instance) {
        String row = formatToRow(instance);

        fm.writeFile(fileName, row);
    }

    public void update(T updatedInstance) {
        int targetInstanceRowNumber = findRowById(updatedInstance.getId());

        String updatedRowContent = formatToRow(updatedInstance);

        fm.editFile(fileName, targetInstanceRowNumber, updatedRowContent);
    }

    public T find(String targetInstanceId) {
        String[] rows = getRows();

        for (String rowData : rows) {
            String[] columns = rowData.split(",");

            if (columns.length > 0 && columns[0].trim().equals(targetInstanceId)) {
                return parseRow(columns);
            }
        }

        throw new IllegalArgumentException("Instance not found: " + targetInstanceId);
    }

    public void delete(T instanceToRemove) {
        int targetRowNumber = findRowById(instanceToRemove.getId());

        fm.removeRow(fileName, targetRowNumber);
    }

    public List<T> getAll() {
        List<T> instances = new ArrayList<>();

        for (String row : getRows()) {
            if (row.trim().isEmpty()) {
                continue;
            }
            String[] columns = row.split(",");
            if (columns.length == 0) {
                continue;
            }
            instances.add(parseRow(columns));
        }

        return instances;
    }

    protected String[] getRows() {
        return fm.readFile(fileName);
    }

    protected abstract String formatToRow(T instance);

    protected abstract int findRowById(String targetInstanceId);

    protected abstract T parseRow(String[] columns);
}
