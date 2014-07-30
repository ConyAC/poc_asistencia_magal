package cl.ps.asistencia.ui;

import org.vaadin.teemu.wizards.WizardStep;

import com.vaadin.ui.Component;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.TextField;

public class AgregarParametros implements WizardStep {

	@Override
	public String getCaption() {
		return "Agregar parámetros";
	}
	
	FormLayout form;
	TextField centroCosto,nombreObra;
	@Override
	public Component getContent() {
		form = new FormLayout();
		
		centroCosto = new TextField("Centro de Costo");
		nombreObra = new TextField("Nombre Obra");
		DateField datefield = new DateField("Fecha(Mes)");
		form.addComponent(centroCosto);
		form.addComponent(nombreObra);
		form.addComponent(datefield);
		
		return form;
	}

	@Override
	public boolean onAdvance() {
		GenerarExcel.cc = centroCosto.getValue();
		GenerarExcel.no = nombreObra.getValue();
		return true;
	}

	@Override
	public boolean onBack() {
		return true;
	}

}
