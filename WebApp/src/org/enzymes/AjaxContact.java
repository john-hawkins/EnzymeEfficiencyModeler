package org.enzymes;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class MainSearch
 */
public class AjaxContact extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AjaxContact() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        String name = request.getParameter("name").toString();
        String email = request.getParameter("email").toString();
        String subject = request.getParameter("subject").toString();
        String message = request.getParameter("message").toString();
        System.out.println("Submitted Contact From: " + name );

        //out.println("<p>Thank you" + name + ", your message was successfully sent</p>");

        out.println("<fieldset class='important'>");
        out.println("<legend>Message Successfully Sent</legend>");
        out.println("<span>");
        out.println("	<strong>Thank you " + name + ". We will repsond shortly</strong>");
        out.println("</span>");
        out.println("</fieldset>");
         
        out.close();
	}

}

