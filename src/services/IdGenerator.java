/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;

import java.util.Map;
import models.IdMeta;
import models.Item;
import models.Payment;
import models.PurchaseOrder;
import models.PurchaseRequisition;
import models.Sales;
import models.Supplier;
import models.users.User;

/**
 *
 * @author Chan Yong Liang
 */
public class IdGenerator {
    private static final Map<Class<?>, IdMeta> idMetaMap = Map.of(
            Item.class, new IdMeta("i", 3, "itemsId"),
            User.class, new IdMeta("u", 3, "usersId"),
            Supplier.class, new IdMeta("su", 3, "supplierId"),
            Payment.class, new IdMeta("p", 3, "paymentsId"),
            PurchaseOrder.class, new IdMeta("s", 3, "purchase_ordersId"),
            PurchaseRequisition.class, new IdMeta("s", 3, "purchase_requisitionsId"),
            Sales.class, new IdMeta("s", 3, "salesId")
    );

    FileManager fm = new FileManager();

    public String generateNewId(Class<?> modelClass) {
        IdMeta meta = idMetaMap.get(modelClass);

        // if not found, check is the passes class a subclass of a key
        if (meta == null) {
            for (Map.Entry<Class<?>, IdMeta> entry : idMetaMap.entrySet()) {
                if (entry.getKey().isAssignableFrom(modelClass)) {
                    meta = entry.getValue();
                    break;
                }
            }
        }

        if (meta == null) {
            throw new IllegalArgumentException("No ID metadata defined for: " + modelClass.getSimpleName());
        }

        String lastId = fm.readFile(meta.fileName)[0];
        int lastRawId = Integer.parseInt(lastId);
        int newRawId = lastRawId + 1;

        String newId = meta.prefix + String.format("%0" + meta.padLength + "d", newRawId);

        fm.clearFile(meta.fileName);
        fm.writeFile(meta.fileName, Integer.toString(newRawId));

        return newId;
    }

}
