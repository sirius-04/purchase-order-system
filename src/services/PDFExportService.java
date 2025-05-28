/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package services;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.jfree.chart.JFreeChart;

/**
 *
 * @author ngoh
 */
public class PDFExportService {
    public void exportChartToPDF(JFreeChart chart, String title) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save PDF Report");
        fileChooser.setFileFilter(new FileNameExtensionFilter("PDF Files", "pdf"));

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String defaultName = title.replaceAll(" ", "_") + "_" + dateFormat.format(new Date()) + ".pdf";
        fileChooser.setSelectedFile(new File(defaultName));
        
        int userSelection = fileChooser.showSaveDialog(null);
        
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            String filePath = fileToSave.getAbsolutePath();
            
            if (!filePath.toLowerCase().endsWith(".pdf")) {
                filePath += ".pdf";
            }
            
            try {
                // Create PDF document
                Document document = new Document();
                PdfWriter.getInstance(document, new FileOutputStream(filePath));
                document.open();
                
                // Add title
                Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
                Paragraph titlePara = new Paragraph(title, titleFont);
                titlePara.setAlignment(Paragraph.ALIGN_CENTER);
                titlePara.setSpacingAfter(20f);
                document.add(titlePara);
                
                // Add date
                Font dateFont = FontFactory.getFont(FontFactory.HELVETICA, 12);
                Paragraph datePara = new Paragraph("Generated on: " + new Date(), dateFont);
                datePara.setAlignment(Paragraph.ALIGN_CENTER);
                datePara.setSpacingAfter(20f);
                document.add(datePara);
                
                // Convert chart to image
                BufferedImage bufferedImage = chart.createBufferedImage(800, 600);
                Image pdfImage = Image.getInstance(bufferedImage, null);
                pdfImage.setAlignment(Image.ALIGN_CENTER);
                
                // Scale image to fit page
                float scale = Math.min(
                    document.getPageSize().getWidth() / pdfImage.getWidth(),
                    document.getPageSize().getHeight() / pdfImage.getHeight()
                );
                pdfImage.scalePercent(scale * 90); // 90% of available space
                
                document.add(pdfImage);
                document.close();
                
                JOptionPane.showMessageDialog(null, "PDF exported successfully to:\n" + filePath);
            } catch (DocumentException | IOException e) {
                JOptionPane.showMessageDialog(null, "Error exporting PDF: " + e.getMessage(), 
                    "Export Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
