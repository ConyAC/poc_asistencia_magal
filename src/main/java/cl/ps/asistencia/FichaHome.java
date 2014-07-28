package cl.ps.asistencia;

import com.vaadin.annotations.Theme;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener.ViewChangeEvent;
import com.vaadin.server.FontAwesome;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@Theme("dashboard")
public class FichaHome extends Panel implements View {

	/**
	 * 
	 */
	private static final long serialVersionUID = 16569948508628877L;

    public static final String FICHAVIEW = "ficha";
	
	public FichaHome(){
		
		//utiliza todo el espacio
		setSizeFull();
		addStyleName("dashboard-view");
		//crea el contenedor
		CssLayout raiz = new CssLayout();
		raiz.setSizeFull();
		raiz.addStyleName("view-content");
		raiz.addStyleName("main-view");
		setContent(raiz);
		
		HorizontalLayout top = drawTop();
		raiz.addComponent(top);
		
		HorizontalSplitPanel vsl = drawContent(); 
				
		raiz.addComponent(vsl);
		
	}
	

	private HorizontalSplitPanel drawContent() {
		HorizontalSplitPanel hsp = new HorizontalSplitPanel();
		hsp.addStyleName("layout-panel");
		hsp.setSplitPosition(15);
		
		hsp.setFirstComponent(new CssLayout(){{
			for(String s : new String[]{ "jose perez" ,"Perez jose"} ){
				addComponent(new Label(s));
			}
		}});
		
		hsp.setSecondComponent(new CssLayout(){{
			HorizontalLayout top = new HorizontalLayout();
			top.setSpacing(true);
			addComponent(top);
			
			// A theme resource in the current theme ("mytheme")
			// Located in: VAADIN/themes/mytheme/img/themeimage.png
			ThemeResource resource = new ThemeResource("img/915.JPG");
			Image img = new Image(null, resource);
			img.setWidth("140px");
			img.setHeight("160px");
			top.addComponent(img);
			
			VerticalLayout vl = new VerticalLayout();
			top.addComponent(vl);
			
			vl.addComponent(new Label("<h1>Jose Perez</h1>",ContentMode.HTML));
			vl.addComponent(new Label("<h3>111111-1</h3>",ContentMode.HTML));
			
			
			
			
			
		}});

		return hsp;
	}


	private HorizontalLayout drawTop() {
		HorizontalLayout hl = new HorizontalLayout();
		hl.setWidth("100%");
		hl.setHeight("50px");
		hl.setSpacing(false);
		
		TextField nombre = new TextField("Nombre");
		hl.addComponent(nombre);
		hl.setComponentAlignment(nombre, Alignment.BOTTOM_LEFT);
		
		Button btn = new Button(null,FontAwesome.SEARCH );
		hl.addComponent(btn);
		hl.setComponentAlignment(btn, Alignment.BOTTOM_LEFT);
		
		ComboBox cb = new ComboBox("Estado");
		cb.addItem("Vigente");
		cb.addItem("No Vigente");
		cb.setNullSelectionAllowed(false);
		cb.select("Vigente");
		hl.addComponent(cb);
		
		
		//boton  para volver al csv
		Button ficha = new Button("Volver");
        ficha.addStyleName("link");
        hl.addComponent(ficha);
        hl.setComponentAlignment(ficha, Alignment.BOTTOM_RIGHT);
        
        ficha.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				UI.getCurrent().getNavigator().navigateTo(CSV.CSV);
			}
		});
		
		return hl;
	}


	@Override
	public void enter(ViewChangeEvent event) {

	}

}
