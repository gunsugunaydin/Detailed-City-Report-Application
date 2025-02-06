package org.gunsugunaydin.DetailedCityReport.payload;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CityData {   
    private String name;
    private double latitude;
    private double longitude;
    private String country;
    private int population;
    private String region;
    private boolean is_capital;

    public boolean isIs_capital() {
        return is_capital;
    }
    public void setIs_capital(boolean is_capital) {
        this.is_capital = is_capital;
    }
}