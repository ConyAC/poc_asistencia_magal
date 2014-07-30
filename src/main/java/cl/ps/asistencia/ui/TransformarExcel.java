package cl.ps.asistencia.ui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.vaadin.teemu.wizards.WizardStep;

import com.vaadin.server.FileDownloader;
import com.vaadin.server.FileResource;
import com.vaadin.server.Resource;
import com.vaadin.server.VaadinService;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.Upload;
import com.vaadin.ui.VerticalLayout;

public class TransformarExcel implements WizardStep {

	@Override
	public String getCaption() {
		return "Transformar Excel a Softland";
	}

	protected File tempFile;
	
	@Override
	public Component getContent() {
		
		
		VerticalLayout contenido = new VerticalLayout();
		contenido.addComponent(new Label("<br />",ContentMode.HTML));
		contenido.setSpacing(true);
		
		/* Create and configure the upload component */
       Upload upload = new Upload(null, new Upload.Receiver() {
         @Override
         public OutputStream receiveUpload(String filename, String mimeType) {
           try {
             /* Here, we'll stored the uploaded file as a temporary file. No doubt there's
               a way to use a ByteArrayOutputStream, a reader around it, use ProgressListener (and
               a progress bar) and a separate reader thread to populate a container *during*
               the update.
    
               This is quick and easy example, though.
               */
             tempFile = File.createTempFile("temp2", ".xls");
             return new FileOutputStream(tempFile);
           } catch (IOException e) {
             e.printStackTrace();
             return null;
           }
         }
       });
       
       String basepath = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();
       final File actualFile = new File(basepath +"\\VAADIN\\descargas\\softland.csv");
       
       final Button saveExcel = new Button("Generar archivo Softland - Debe subir archivo excel modificado");
       saveExcel.setEnabled(false);
       
       upload.addFinishedListener(new Upload.FinishedListener() {
         @Override
         public void uploadFinished(Upload.FinishedEvent finishedEvent) {
           try {
             /* Let's build a container from the CSV File */
             FileInputStream file = new FileInputStream(tempFile);
             FileOutputStream out = new FileOutputStream(actualFile);
             HSSFWorkbook workbook = new HSSFWorkbook(file);
             //Get first sheet from the workbook
             HSSFSheet sheet = workbook.getSheetAt(0);
              
             //Iterate through each rows from first sheet
             Iterator<Row> rowIterator = sheet.iterator();
             while(rowIterator.hasNext()) {
                 Row row = rowIterator.next();
                  
                 //For each row, iterate through each columns
                 Iterator<Cell> cellIterator = row.cellIterator();
                 while(cellIterator.hasNext()) {
                      
                     Cell cell = cellIterator.next();
                      
                     switch(cell.getCellType()) {
                         case Cell.CELL_TYPE_BOOLEAN:
                        	 out.write((cell.getBooleanCellValue() + ";\t\t").getBytes());
                             break;
                         case Cell.CELL_TYPE_NUMERIC:
                        	 out.write((cell.getNumericCellValue() + ";\t\t").getBytes());
                             break;
                         case Cell.CELL_TYPE_STRING:
                             out.write((cell.getStringCellValue() + ";\t\t").getBytes());
                             break;
                     }
                 }
                 out.write("\n".getBytes());
             }
             file.close();
             out.close();
             tempFile.delete();
             saveExcel.setCaption("Generar archivo Softland");
             saveExcel.setEnabled(true);
    
           } catch (IOException e) {
             e.printStackTrace();
           }
         }
       });
    
        contenido.addComponent(upload);
      
		Resource res = new FileResource(actualFile);
		FileDownloader fd = new FileDownloader(res);
		fd.extend(saveExcel);
		contenido.addComponent(saveExcel);
		
		return contenido;
		

	}

	@Override
	public boolean onAdvance() {
		return false;
	}

	@Override
	public boolean onBack() {
		return true;
	}

}
