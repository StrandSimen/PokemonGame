package simen.order.loadDB.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ApiResponse {
    private List<Card> data;

    public List<Card> getData() { return data; }
    public void setData(List<Card> data) { this.data = data; }
}
