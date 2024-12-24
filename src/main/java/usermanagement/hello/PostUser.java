package usermanagement.hello;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.json.Json;
import javax.json.JsonObject;

@ManagedBean(name = "userBean")
@RequestScoped
public class PostUser {

	private String nome;
	private String email;
	private String senha;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public void criarUsuario() {
		try {
	        URL url = new URL("http://localhost:8081/api/users");

	        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
	        connection.setRequestMethod("POST");
	        connection.setRequestProperty("Content-Type", "application/json");
	        connection.setDoOutput(true);

	        JsonObject json = Json.createObjectBuilder()
	                .add("nome", nome)
	                .add("email", email)
	                .add("senha", senha)
	                .build();

	        try (OutputStream os = connection.getOutputStream()) {
	            byte[] input = json.toString().getBytes("utf-8");
	            os.write(input, 0, input.length);
	        }

	        int responseCode = connection.getResponseCode();
	        if (responseCode == HttpURLConnection.HTTP_CREATED) {
	            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Usuário criado com sucesso!"));
	        } else {
	            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Erro ao criar o usuário."));
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Erro ao criar o usuário: " + e.getMessage()));
	    }
    }
}