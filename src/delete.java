

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;


@WebServlet("/delete")
public class delete extends HttpServlet {
    private static final long serialVersionUID = 1L;
    String saveDirectory = "/images_uploaded"; 

    
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
    	
PrintWriter out = res.getWriter();
        
        String[] ch = req.getParameterValues("ch"); 
        String saveDirectory = "/images_uploaded";
        String realPath = getServletContext().getRealPath(saveDirectory);
        File fsaveDirectory = new File(realPath);
        
        out.println("<!DOCTYPE html>\n" + "<html lang=\"en\">" + "<body>" + "<head>" + "<link rel=\"stylesheet\" href=\"css.css\">");
        out.println("</head>\n" + "<body>");
        out.println("<pre>");
        
        for(int i=0;i<ch.length;i++){
        	
        	File ff = new File(fsaveDirectory, ch[i]);
        out.println("ch: " + ch[i]+ "<br>");    
                
        ff.delete();
        out.println("ch: " + ch[i]+ "delete"+"<br>"); 
        
        }  
        out.println("</pre>");
        out.println("</body>" + "</html>");
    }

    

    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        
    }

}
