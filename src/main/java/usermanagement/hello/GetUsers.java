package usermanagement.hello;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;

@ManagedBean(name = "helloBean")
@RequestScoped
public class GetUsers implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private List<User> users = new ArrayList<>();

    @PostConstruct
    public void init() {
        loadUsersFromApi();
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    private void loadUsersFromApi() {
        try {
            
            String apiUrl = "http://localhost:8081/api/users";
           
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000); 

            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();
                
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                String jsonResponse = response.toString();
                JsonArray jsonArray = Json.createReader(new StringReader(jsonResponse)).readArray();

                for (JsonObject jsonObject : jsonArray.getValuesAs(JsonObject.class)) {
                    int id = jsonObject.getInt("id");
                    String nome = jsonObject.getString("nome");
                    String email = jsonObject.getString("email");
                    String dataCadastroString = jsonObject.getString("dataCadastro");
                    DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
                    LocalDateTime dataCadastro = LocalDateTime.parse(dataCadastroString, formatter);
                    users.add(new User(id, nome, email, dataCadastro));
                }
                
            } else {
                System.out.println("Erro ao conectar com a API. Código de resposta: " + connection.getResponseCode());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}