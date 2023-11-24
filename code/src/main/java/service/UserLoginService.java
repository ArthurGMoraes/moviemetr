package service;

import java.util.List;

import dao.UserDAO;
import model.Usuario;
import spark.Request;
import spark.Response;
import java.security.*;
import java.math.*;

public class UserLoginService {

    private static UserDAO userDAO = new UserDAO();
    private String form;


    public Object makeForm(Request request, Response response) {
        // Carregue o formulário de login (HTML) e substitua o valor de 'form' aqui
        // O código do formulário de login deve estar aqui
        form = "                <form id=\"formularioLogin\" class=\"formulario\" action=\"/logar\" method=\"post\">\r\n" + //
                "                    <label for=\"nome\">Nome:</label>\r\n" + //
                "                    <input type=\"text\" name=\"nome\" placeholder=\"Nome\" required>\r\n" + //
                "            \r\n" + //
                "                    <label for=\"senha\">Senha:</label>\r\n" + //
                "                    <input type=\"password\" name=\"senha\" placeholder=\"Senha\" required>\r\n" + //
                "            \r\n" + //
                "                    <input type=\"submit\" value=\"Logar\">\r\n" + //
                "                     <button type=\"button\" onclick=\"window.location.href='/criarConta'\">Registre-se</button>" +
                "                </form>";
        
        return form;
    }

    public Object login(Request request, Response response) throws NoSuchAlgorithmException {
        String nome = request.queryParams("nome");
        String senha = request.queryParams("senha");
        MessageDigest m=MessageDigest.getInstance("MD5");
        m.update(senha.getBytes(),0,senha.length()); 
        senha = new BigInteger(1,m.digest()).toString(16);
        
        // Check if the user exists in the database
        Usuario user = userDAO.getByNome(nome);

        if (user != null && user.getSenha().equals(senha)) {
            // Create a session and store user information
            request.session().attribute("authenticatedUser", user.toString());

            // Redirect to the main page or any other desired destination
            response.redirect("/");
            return "ok";
        } else {
            // Authentication failed
            response.status(401); // Unauthorized
            return "Email ou senha inválidos.";
        }
    }
    

    public Object getAll(Request request, Response response) {
        return form;
    }
}
