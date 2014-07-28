package cl.ps.asistencia;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Reader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.com.bytecode.opencsv.CSVReader;

import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.ui.Upload;
import com.vaadin.ui.VerticalLayout;

public class CSV extends Panel implements View {
	
	Logger logger = LoggerFactory.getLogger(CSV.class);
	
    /**
	 * 
	 */
	private static final long serialVersionUID = 1932184562292323699L;
	
	public static final String CSV = "csv";
	
    protected File tempFile;
    protected Table table;
    
	public CSV(){
		
		//utiliza todo el espacio
		setSizeFull();

   	 /* Create and configure the upload component */
       Upload upload = new Upload("Subir CSV File", new Upload.Receiver() {
         @Override
         public OutputStream receiveUpload(String filename, String mimeType) {
           try {
             /* Here, we'll stored the uploaded file as a temporary file. No doubt there's
               a way to use a ByteArrayOutputStream, a reader around it, use ProgressListener (and
               a progress bar) and a separate reader thread to populate a container *during*
               the update.
    
               This is quick and easy example, though.
               */
             tempFile = File.createTempFile("temp", ".csv");
             return new FileOutputStream(tempFile);
           } catch (IOException e) {
             e.printStackTrace();
             return null;
           }
         }
       });
       upload.addFinishedListener(new Upload.FinishedListener() {
         @Override
         public void uploadFinished(Upload.FinishedEvent finishedEvent) {
           try {
             /* Let's build a container from the CSV File */
             FileReader reader = new FileReader(tempFile);
             IndexedContainer indexedContainer = buildContainerFromCSV(reader);
             reader.close();
             tempFile.delete();
    
             /* Finally, let's update the table with the container */
             table.setCaption(finishedEvent.getFilename());
             table.setContainerDataSource(indexedContainer);
             table.setVisible(true);
           } catch (IOException e) {
             e.printStackTrace();
           }
         }
       });
    
       /* Table to show the contents of the file */
       table = new Table();
       table.setSizeFull();
       table.setVisible(false);
    
       /* Main layout */
       VerticalLayout layout = new VerticalLayout();
       layout.setMargin(true);
       layout.setSpacing(true);
       layout.addComponent(table);
       layout.addComponent(upload);
       
       Button ficha = new Button("Ir a Ficha de Empleados");
       ficha.addStyleName("link");
       layout.addComponent(ficha);
       
       ficha.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				UI.getCurrent().getNavigator().navigateTo(FichaHome.FICHAVIEW);
			}
		});
    
       /* Build the main window */
       setContent(layout);
	}

	@Override
	public void enter(ViewChangeEvent event) {
		// TODO Auto-generated method stub
		
	}
	

    /**
     * Uses http://opencsv.sourceforge.net/ to read the entire contents of a CSV
     * file, and creates an IndexedContainer from it
     *
     * @param reader
     * @return
     * @throws IOException
     */
    protected IndexedContainer buildContainerFromCSV(Reader reader) throws IOException {
      IndexedContainer container = new IndexedContainer();
      CSVReader csvReader = new CSVReader(reader,';');
      String[] columnHeaders = null;
      String[] record;
      while ((record = csvReader.readNext()) != null) {
        if (columnHeaders == null) {
          columnHeaders = record;
          addItemProperties(container, columnHeaders);
        } else {
          addItem(container, columnHeaders, record);
        }
      }
      csvReader.close();
      return container;
    }
   
   
    /**
     * Set's up the item property ids for the container. Each is a String (of course,
     * you can create whatever data type you like, but I guess you need to parse the whole file
     * to work it out)
     *
     * @param container The container to set
     * @param columnHeaders The column headers, i.e. the first row from the CSV file
     */
    private void addItemProperties(IndexedContainer container, String[] columnHeaders) {
  	  logger.debug("size "+columnHeaders.length );
      for (String propertyName : columnHeaders) {
      	logger.debug(" propertyName "+propertyName);
        container.addContainerProperty(propertyName, String.class, null);
      }
      logger.debug(" ");
    }
   
    /**
     * Adds an item to the given container, assuming each field maps to it's corresponding property id.
     * Again, note that I am assuming that the field is a string.
     *
     * @param container
     * @param propertyIds
     * @param fields
     */
    private void addItem(IndexedContainer container, String[] propertyIds, String[] fields) {
  	  logger.debug("propertyIds.length "+propertyIds.length+ " fields.length "+ fields.length );
      if (propertyIds.length != fields.length) {
        throw new IllegalArgumentException("Hmmm - Different number of columns to fields in the record");
      }
      Object itemId = container.addItem();
      Item item = container.getItem(itemId);
      for (int i = 0; i < fields.length; i++) {
        String propertyId = propertyIds[i];
        String field = fields[i];
        item.getItemProperty(propertyId).setValue(field);
      }
    }

}
