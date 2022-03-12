package com.robin.weather.views;

import java.util.ArrayList;

import com.robin.weather.controller.WeatherService;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.ClassResource;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import javax.swing.*;

@SpringUI(path="")
public class MainView extends UI
{
	@Autowired
	private WeatherService weatherService;
    private VerticalLayout mainLayout;
    private NativeSelect<String> unitSelect;
    private TextField cityTextField; 
	private Button searchButton;
	private HorizontalLayout dashboard;
	private Label location;
	private Label currentTemp;
	private HorizontalLayout mainDescriptionLayout;
	private Label weatherDescription;
	private Label maxWeather;
	private Label minWeather;
	private Label humidity;
	private Label pressure;
	private Label wind;
	private Label feelsLike;
	private Image iconImg;
    
    @Override
	protected void init(VaadinRequest request) {
		// TODO Auto-generated method stub
		mainLayout();
		setHeader();
		setLogo();
		setForm();
		dashboardTitle();
		dashboardDetails();
		searchButton.addClickListener(clickEvent -> {
			if(!cityTextField.getValue().equals(""))
				updateUI();
			else
				Notification.show("Please enter city name");
		});
	}

	private void updateUI() {
    	String city=cityTextField.getValue();
    	String defaultUnit;
    	weatherService.setCityName(city);
    	if(unitSelect.getValue().equals("°F")){
    		weatherService.setUnit("imperials");
    		unitSelect.setValue("F");
    		defaultUnit="\u00b0"+"F";
		}else {
    		weatherService.setUnit("metric");
    		defaultUnit="\u00b0"+"C";
    		unitSelect.setValue("C");
		}
    	location.setValue("Currently in "+city);
		JSONObject mainObj=weatherService.returnMain();
		int temp=mainObj.getInt("temp");
		currentTemp.setValue(temp+defaultUnit);

		//getting Icon
		String iconCode=null;
		String weatherDescriptionNew=null;
		JSONArray jsonArray=weatherService.returnWeatherArray();
		for(int i=0;i<jsonArray.length();i++)
		{
			JSONObject weatherObject=jsonArray.getJSONObject(i);
			iconCode=weatherObject.getString("icon");
			weatherDescriptionNew=weatherObject.getString("description");
			System.out.println(weatherDescriptionNew);
			
		}
		iconImg.setSource(new ExternalResource("http://openweathermap.org/img/wn/"+iconCode+".png"));
		weatherDescription.setValue("Description: "+weatherDescriptionNew);
		minWeather.setValue("Min Temp: "+weatherService.returnMain().getInt("temp_min")+defaultUnit);
		maxWeather.setValue("Max Temp: "+weatherService.returnMain().getInt("temp_max")+defaultUnit);
		pressure.setValue("Pressure: "+weatherService.returnMain().getInt("pressure")+" mbar");
		humidity.setValue("Humidity: "+weatherService.returnMain().getInt("humidity")+"%");
		wind.setValue("Wind: "+weatherService.returnWind().getInt("speed")+" "+weatherService.convertDegreeToCardinalDirection(weatherService.returnWind().getInt("deg")));
		feelsLike.setValue("FeelsLike: "+weatherService.returnMain().getInt("feels_like"));
		//System.out.println(weatherService.returnMain().getInt("temp_min"));
		mainLayout.addComponents(dashboard,mainDescriptionLayout);

	}

	private void mainLayout() {
		// TODO Auto-generated method stub
		iconImg=new Image();
		mainLayout=new VerticalLayout();
		mainLayout.setWidth("100%");
		mainLayout.setSpacing(true);
		mainLayout.setMargin(true);
		mainLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		setContent(mainLayout);
	}
	private void setHeader() {
		HorizontalLayout header=new HorizontalLayout();
		header.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		Label title=new Label("Weather App By Robin");
		title.addStyleName(ValoTheme.LABEL_H2);
		title.addStyleName(ValoTheme.LABEL_LIGHT);
		title.addStyleName(ValoTheme.LABEL_COLORED);
		header.addComponent(title);

		mainLayout.addComponents(header);
	}
	private void setLogo()
	{
		HorizontalLayout logo=new HorizontalLayout();
		logo.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		Image img=new Image(null,new ClassResource("/static/logo.png"));
		logo.setWidth("240px");
		logo.setHeight("240px");
		logo.addComponent(img);
		mainLayout.addComponents(logo);
	}
	private void setForm()
	{
		HorizontalLayout formLayout=new HorizontalLayout();
		formLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		formLayout.setSpacing(true);
		formLayout.setMargin(true);
		//selection Component
		unitSelect =new NativeSelect<>();
		ArrayList<String> items=new ArrayList<>();
		items.add("°C");
		items.add("°F");
		 unitSelect.setItems(items);
		 unitSelect.setValue(items.get(0));
		 formLayout.addComponent(unitSelect);
		 
		 //cityTextField
		 cityTextField=new TextField();
		 cityTextField.setWidth("80%");
		 formLayout.addComponent(cityTextField);
		 
		 //search button
		 searchButton=new Button();
		 searchButton.setIcon(VaadinIcons.SEARCH);
		 formLayout.addComponent(searchButton);
		 
		 
		 mainLayout.addComponents(formLayout);
	}
	
	private void dashboardTitle()
	{
		dashboard= new HorizontalLayout();
		dashboard.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		
		//city location
		location=new Label("Cuurently in new delhi");
		location.addStyleName(ValoTheme.LABEL_H2);
		location.addStyleName(ValoTheme.LABEL_LIGHT);
		
		//current TEMP
		currentTemp=new Label("18°C");
		currentTemp.setStyleName(ValoTheme.LABEL_BOLD);
		currentTemp.setStyleName(ValoTheme.LABEL_H1);
		
		dashboard.addComponents(location,iconImg,currentTemp);
		
		
		//
		
	}
	
	private void dashboardDetails()
	{
		mainDescriptionLayout=new HorizontalLayout();
		mainDescriptionLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		
		//description layout
		VerticalLayout descriptionLayout=new VerticalLayout();
		descriptionLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		
		//weather description
		weatherDescription=new Label("Description: Sunny");
		weatherDescription.setStyleName(ValoTheme.LABEL_SUCCESS);
		descriptionLayout.addComponent(weatherDescription);

		//Min Temp
		minWeather=new Label("Minimum Temprature: 9°C");
		minWeather.setStyleName(ValoTheme.LABEL_LIGHT);
		descriptionLayout.addComponent(minWeather);

		//Max Temp
		maxWeather=new Label("MaximumTemprature: 14°C");
		maxWeather.setStyleName(ValoTheme.LABEL_LIGHT);
		descriptionLayout.addComponent(maxWeather);

		VerticalLayout pressureLayout=new VerticalLayout();
		pressureLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

		pressure=new Label("pressure: 230pa");
		pressureLayout.addComponent(pressure);

		humidity=new Label("humidity: 13%");
		pressureLayout.addComponent(humidity);

	    feelsLike = new Label("feelsLike: 230pa");
		pressureLayout.addComponent(feelsLike);

		wind=new Label("wind: 230pa");
		pressureLayout.addComponent(wind);

		mainDescriptionLayout.addComponents(descriptionLayout,pressureLayout);
	//	mainLayout.addComponent(mainDescriptionLayout);
	}
	
	
	

}
