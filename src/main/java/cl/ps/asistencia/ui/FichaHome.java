package cl.ps.asistencia.ui;

import java.util.Arrays;
import java.util.List;

import com.vaadin.annotations.Theme;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
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
import com.vaadin.ui.NativeButton;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Panel;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;

@Theme("dashboard")
public class FichaHome extends Panel implements View {

	/**
	 * 
	 */
	private static final long serialVersionUID = 16569948508628877L;

    public static final String NAME = "ui_hoja_de_vida";
	
	public FichaHome(){
		
		//utiliza todo el espacio
		setSizeFull();
		addStyleName("dashboard-view");
		
		VerticalLayout root = new VerticalLayout();
        root.setSizeFull();
        root.setSpacing(true);
        setContent(root);
        
      //agrega la parte de arriba con el título y la información
	    HorizontalLayout top = drawTop();
	    top.setWidth("95%");
	    root.addComponent(top);
	    
	    VerticalLayout vl = new VerticalLayout();
	    vl.setSizeFull();
		
	    CssLayout filtro = drawFiltro();
		
		CssLayout vsl = drawContent(); 
		
	    vl.addComponent(filtro);
	    vl.addComponent(vsl);
	    vl.setExpandRatio(vsl, 1.0F);
	    
	    root.addComponent(vl);
	    root.setExpandRatio(vl, 1.0F);
		
	}
	
	private HorizontalLayout drawTop() {

		HorizontalLayout top = new HorizontalLayout();
        top.setWidth("100%");
        top.setSpacing(true);
        top.setMargin(false);
        
        Label title = new Label(
        		"<span class='v-label h1' >Hojas de vida</span><br />",
        		ContentMode.HTML);
        
        title.setSizeUndefined();
        top.addComponent(title);
        top.setComponentAlignment(title, Alignment.MIDDLE_LEFT);
        
        return top;
	    
	}
	
	HorizontalSplitPanel hsp;

	private CssLayout drawContent() {
		
		CssLayout contenido = new CssLayout();
		contenido.setSizeFull();
		contenido.addStyleName("layout-panel");
		
		hsp = new HorizontalSplitPanel();
		contenido.addComponent(hsp);
		hsp.setSplitPosition(15);
		
		hsp.setFirstComponent(
				new VerticalLayout(){{
					setWidth("100%");
					addStyleName("sidebar2");
					addComponent(
							new CssLayout(){{
								addStyleName("menu2"); 
								for(final Viejo s : viejos){
									Button b = new NativeButton(s.getNombre());
									if(s.getCarita() == 0)
										b.setIcon(FontAwesome.SMILE_O);
									else if(s.getCarita() == 1)
										b.setIcon(FontAwesome.FROWN_O);
									else
										b.setIcon(FontAwesome.MEH_O);
									
									b.addClickListener(new Button.ClickListener() {
										
										@Override
										public void buttonClick(ClickEvent event) {
											setContent(s);
											
										}
									});
									addComponent(b);

								}
						}});
								
				}});
		setContent(viejos.get(0));
		return contenido;
	}
	
	List<Viejo> viejos = Arrays.asList(
			new Viejo("Jose Perez","11111-1","915.JPG",0),
			new Viejo("Jorge Saavedra","222222-1","914.JPG",1),
			new Viejo("Patricio Torres","3333333-1","41.JPG",2),
			new Viejo("Carlos Cornejo","4444444-1","4.JPG",1)
			);
	
	private void setContent(final Viejo viejo){
		hsp.setSecondComponent(new CssLayout(){{
			HorizontalLayout top = new HorizontalLayout();
			top.setSpacing(true);
			addComponent(top);
			
			// A theme resource in the current theme ("mytheme")
			// Located in: VAADIN/themes/mytheme/img/themeimage.png
			ThemeResource resource = new ThemeResource("img/"+viejo.getFoto());
			Image img = new Image(null, resource);
			img.setWidth("140px");
			img.setHeight("160px");
			top.addComponent(img);
			
			VerticalLayout vl = new VerticalLayout();
			top.addComponent(vl);
			
			vl.addComponent(new Label("<h1>"+viejo.getNombre()+"</h1>",ContentMode.HTML));
			vl.addComponent(new Label("<h3>"+viejo.getRut()+"</h3>",ContentMode.HTML));
			vl.addComponent(new Label("<p>Otra información......</p>",ContentMode.HTML));
			
		}});
	}


	private CssLayout drawFiltro() {
		
		CssLayout contenido = new CssLayout();
		contenido.setHeight("140px");
		contenido.addStyleName("layout-panel");
		
		HorizontalLayout hl = new HorizontalLayout();
		contenido.addComponent(hl);
		hl.setWidth("100%");
		hl.setSpacing(false);
		
		HorizontalLayout hl2 = new HorizontalLayout();
		hl2.setSpacing(true);
		final TextField nombre = new TextField("Nombre");
		hl2.addComponent(nombre);
		hl2.setComponentAlignment(nombre, Alignment.BOTTOM_LEFT);
		
		Button btn = new Button(null,FontAwesome.SEARCH );
		btn.addClickListener(new Button.ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				Notification.show("Filtrando por nombre "+nombre.getValue(),Type.HUMANIZED_MESSAGE);
			}
		});
		hl2.addComponent(btn);
		hl2.setComponentAlignment(btn, Alignment.BOTTOM_LEFT);
		
		hl.addComponent(hl2);
		
		final ComboBox cb = new ComboBox("Estado");
		cb.addItem("Vigente");
		cb.addItem("No Vigente");
		cb.setNullSelectionAllowed(false);
		cb.select("Vigente");
		cb.addValueChangeListener(new Property.ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				Notification.show("Filtrando por estado "+cb.getValue(),Type.HUMANIZED_MESSAGE);
			}
		});
		
		hl2.addComponent(cb);
		
		final ComboBox cb2 = new ComboBox("Faena");
		cb2.addItem("Faena1");
		cb2.addItem("Faena2");
		cb2.addItem("Faena3");
		cb2.setNullSelectionAllowed(false);
		cb2.select("Faena1");
		cb2.addValueChangeListener(new Property.ValueChangeListener() {
			
			@Override
			public void valueChange(ValueChangeEvent event) {
				Notification.show("Filtrando por faena "+cb2.getValue(),Type.HUMANIZED_MESSAGE);
			}
		});
		
		hl2.addComponent(cb2);
		
		return contenido;
	}


	@Override
	public void enter(ViewChangeEvent event) {

	}
	
	public class Viejo{
		String nombre;
		String rut;
		int carita;
		String foto;
		
		public Viejo(String nombre, String rut, String foto, int carita) {
			super();
			this.nombre = nombre;
			this.rut = rut;
			this.carita = carita;
			this.foto = foto;
		}
		public String getNombre() {
			return nombre;
		}
		public void setNombre(String nombre) {
			this.nombre = nombre;
		}
		public String getRut() {
			return rut;
		}
		public void setRut(String rut) {
			this.rut = rut;
		}
		public int getCarita() {
			return carita;
		}
		public void setCarita(int carita) {
			this.carita = carita;
		}
		public String getFoto() {
			return foto;
		}
		public void setFoto(String foto) {
			this.foto = foto;
		}
		
	}

}
