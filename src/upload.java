import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;

@WebServlet("/upload")
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 5 * 1024 * 1024, maxRequestSize = 5 * 5 * 1024 * 1024)

public class upload extends HttpServlet {
    private static final long serialVersionUID = 1L;

    String[] names;
    ArrayList<String> filenames = new ArrayList<String>();


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

        req.setCharacterEncoding("Big5"); // 處理中文檔名
        res.setContentType("text/html; charset=Big5");
        int i = 0;

        PrintWriter out = res.getWriter();

        String saveDirectory = "/images_uploaded";
        String realPath = getServletContext().getRealPath(saveDirectory);

        File fsaveDirectory = new File(realPath);
        if (!fsaveDirectory.exists())
            fsaveDirectory.mkdirs(); // 於 ContextPath 之下,自動建立目地目錄

        Collection<Part> parts = req.getParts(); // Servlet3.0新增了Part介面，讓我們方便的進行檔案上傳處理
        ArrayList<File> files = new ArrayList<File>();

        out.write("<FORM action=\"delete\" method=get enctype=\"multipart/form-data\">");

        for (Part part : parts) {

            if (getFileNameFromPart(part) != null && part.getContentType() != null) {

                out.println("<PRE>");
                
                String filename = getFileNameFromPart(part);

                String ContentType = part.getContentType();
                long size = part.getSize();

                File f = new File(fsaveDirectory, filename);
                out.println("filenames: " + filename);
               
                out.println("size: " + size);
                out.println("File: " + f);

                part.write(f.toString());

                // 額外測試 InputStream 與 byte[] (幫將來model的VO預作準備)
                InputStream in = part.getInputStream();
                byte[] buf = new byte[in.available()];
                in.read(buf);
                in.close();
                out.println("buffer length: " + buf.length);

                // 額外測試秀圖
                out.println("<br><img src=\"" + req.getContextPath() + saveDirectory + "/" + filename + "\">");

                out.println("<input type=\"checkbox\" name=\"ch\" value = \""+filename+ "\">");
               
                out.println("</PRE>");
            }
        }


        out.println("<input type=\"submit\">");
        out.println("</FORM>");
    }

    // 取出上傳的檔案名稱 (因為API未提供method,所以必須自行撰寫)
    public String getFileNameFromPart(Part part) {
        String header = part.getHeader("content-disposition");
        System.out.println("header=" + header); // 測試用
        String filename = new File(header.substring(header.lastIndexOf("=") + 2, header.length() - 1)).getName();
        System.out.println("filename=" + filename); // 測試用
        if (filename.length() == 0) {
            return null;
        }
        return filename;
    }

}


