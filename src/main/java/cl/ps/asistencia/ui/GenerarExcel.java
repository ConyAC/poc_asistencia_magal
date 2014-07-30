package cl.ps.asistencia.ui;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.vaadin.teemu.wizards.WizardStep;

import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.server.FileDownloader;
import com.vaadin.server.FileResource;
import com.vaadin.server.Resource;
import com.vaadin.server.VaadinService;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;

public class GenerarExcel implements WizardStep {

	Logger logger = LoggerFactory.getLogger(GenerarExcel.class);
	
	@Override
	public String getCaption() {
		return "Generar Excel";
	}
	
	public static IndexedContainer indexedContainer;
	public static String cc;
	public static String no;

	/**
	 * genera el excel
	 */
	@Override
	public Component getContent() {
		
		String basepath = VaadinService.getCurrent()
	            .getBaseDirectory().getAbsolutePath();
		File actualFile = new File(basepath +"/VAADIN/descargas/CR_4B_Asistencia.xls");
		logger.debug("generar excel");
		try {
			
			File upload = new File(basepath+ "/VAADIN/plantilla/CR_4B_Asistencia_Julio_2014.xls");
			FileInputStream file = new FileInputStream(upload);
			logger.debug("modificando archivo plantilla "+upload.getAbsolutePath());
		    HSSFWorkbook workbook = new HSSFWorkbook(file);
		    //primera hoja
		    HSSFSheet sheet = workbook.getSheetAt(1);
		    sheet.protectSheet("123456");
		    Cell cell = null;
		 
		    CellStyle unlockedCellStyle = workbook.createCellStyle();
		    unlockedCellStyle.setLocked(false);
		    
		    //llena la información del txt
		    if( indexedContainer != null && indexedContainer.size() != 0 ){
		    	int rownum = 8;
		    	int colnum = 3;
		    	for(Object obj : indexedContainer.getItemIds()){
		    		Item item = indexedContainer.getItem(obj);
				    //Update the value of cell
				    Row row = sheet.getRow(rownum);
				    if( row == null )
				    	row = sheet.createRow(rownum);
				    cell = sheet.getRow(rownum).getCell(colnum);
				    if(cell == null ){
				    	cell = sheet.getRow(rownum).createCell(colnum);
				    }
				    int i = 4;
				    for(Object obj2 : item.getItemPropertyIds()){
				    	//agrega la propiedad 4
				    	if( i == 1){
				    		cell.setCellValue(item.getItemProperty(obj2).getValue()+"");
				    		cell.setCellStyle(unlockedCellStyle);
				    	}
				    	i--;
				    }
				    rownum++;
				    if( rownum == 76)
				    	break;
		    	}
		    }
		    if(cc!= null || no != null ){
		    	HSSFSheet sheet2 = workbook.getSheetAt(0);
		    	
		    	if( cc != null ){
		    		Row row = sheet2.getRow(0);
			    	cell = row.getCell(1);
			    	cell.setCellValue(cc);
		    	}
		    	if( no != null ){
		    		Row row = sheet2.getRow(1);
			    	cell = row.getCell(1);
			    	cell.setCellValue(no);
		    	}
		    }
		     
		    file.close();
		     
		    FileOutputStream outFile = new FileOutputStream(actualFile);
		    logger.debug("guardando archivo "+actualFile.getAbsolutePath());
		    workbook.write(outFile);
		    outFile.close();
		    logger.debug("guardado");
		     
		} catch (Exception e) {
		    e.printStackTrace();
		}
		

		Button saveExcel = new Button("Generar Excel");
		Resource res = new FileResource(actualFile);
		FileDownloader fd = new FileDownloader(res);
		fd.extend(saveExcel);
		
		return saveExcel;
	}

	@Override
	public boolean onAdvance() {
		return true;
	}

	@Override
	public boolean onBack() {
		return true;
	}

}
