/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

/**
 *
 * @author t311372
 */
public class Products {
    
    

    
    private String description;
    private String title;
    private Double unit_price;
    private Long link_size;

    public Products() {
    }

    

    public Products(String description, String title, Double unit_price, Long link_size) {
        this.description = description;
        this.title = title;
        this.unit_price = unit_price;
        this.link_size = link_size;
        
        
    }

    public Long getLink_size() {
        return link_size;
    }

    public void setLink_size(Long link_size) {
        this.link_size = link_size;
    }
           

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getUnit_price() {
        return unit_price;
    }

    public void setUnit_price(Double unit_price) {
        this.unit_price = unit_price;
    }

          
}
