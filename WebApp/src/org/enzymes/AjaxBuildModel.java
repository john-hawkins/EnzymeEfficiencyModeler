package org.enzymes;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.*;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class MainSearch
 */
public class AjaxBuildModel extends HttpServlet {
	private static final long serialVersionUID = 1L;

    private static RequestDeleter deleter;
    
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AjaxBuildModel() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * Initialisation method. This is called when the servlet is first accessed.
     * This starts the RequestDeleter task running.
     */
    public void init() throws ServletException{
        System.out.println ("Web Appplication starting up:");
        
		System.setProperty("java.awt.headless","true");
		
        // start request deleter 
        //  (checks for timed out requests and deletes accordingly)
        deleter = new RequestDeleter();
        Timer timer = new Timer(true);
        timer.schedule(deleter, RequestDeleter.runDelay, RequestDeleter.runDelay);
        
        // Initialise the default process. Replace this with a more general mechanism
        // if multiple processes are implemented 
        //  (eg array of processes that need initialising)
        //PProcessDefault.init(getServletContext());
        
        System.out.println ("Startup complete.");
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
        
        String addr = request.getRemoteAddr();
        String host = request.getRemoteHost();
        String action = request.getParameter("action").toString();
        
        if(action.equals("build")) {
            
        	String message = "";
        	boolean validParams = true;
        	
            String model = request.getParameter("model").toString();
            String encoding = request.getParameter("encoding").toString();
            String modelname = request.getParameter("modelname").toString();
            String theSeqs = request.getParameter("sequences").toString();
            int theParam1 = Integer.parseInt(request.getParameter("parameter1").toString());
            

            System.err.println("PROCESSING BUILD REQUEST");
            
            double theParam2 = 0;
            try{
                if( ! request.getParameter("parameter2").toString().equals("")) {
                	theParam2 = Double.parseDouble(request.getParameter("parameter2").toString());
                }
            } catch (Exception e ) {
            	validParams = false;
            	message = "Models paramaters must be numeric values";
            }

            double theParam3 = 0;    
            try{
                if( ! request.getParameter("parameter3").toString().equals("")) {
                	theParam3 = Double.parseDouble(request.getParameter("parameter3").toString());
                }
            } catch (Exception e ) {
            	validParams = false;
            	message = "Models paramaters must be numeric values";
            }
            


            //System.err.println("SEQs {" + theSeqs + "}");
            
            String[] aln = theSeqs.split("~");
            //System.err.println("FIRST PART {" + aln[0] + "}");
            
            String criticalPositions = request.getParameter("positions").toString();
            /*
            String[] possies = request.getParameter("positions").toString().split("-");
            int[][] positions = new int[possies.length][];
            for(int i =0; i<possies.length; i++) {
            	String[] temp = possies[i].split(",");
            	positions[i] = new int[temp.length];
            	for(int t=0; t<temp.length; t++) {
            		positions[i][t] = Integer.parseInt(temp[t]);
            	}
            }*/
            
            String[] efficiencies = request.getParameter("efficiencies").toString().split("~"); 

            String[] sequences = new String[aln.length];
            String[] names = new String[aln.length];
            for(int i =0; i<aln.length; i++) {
            	String[] temp = aln[i].split(" +");
            	names[i] = temp[0].trim();
            	sequences[i] = temp[1].trim();
                //System.err.println("LINE " + i + " NAME {" + names[i] + "} SEQ {" + sequences[i] + "}");
            } 
            
            if(validParams) {
                Request req = new Request(addr, modelname, names, sequences, efficiencies, criticalPositions, model, encoding, theParam1, theParam2, theParam3);
                
                RequestProcessor processor = new RequestProcessor(req);
                
            	Thread processThread = new Thread (processor, "Process for request " + req.getID());
            	processThread.start();

                out.println("<fieldset class='important'>");
            	out.println("<legend>Request Submitted</legend>");
            	out.println("<span class='css'>");
            	out.println("	Your model is being generated. Please use the <b>View Results</b> tab to monitor progress and view the results.");
            	out.println("</span>");
            	out.println("</fieldset>");	

            } else {	
            	out.println("<fieldset class='error'>");
            	out.println("<legend>DATA ERROR</legend>");
            	out.println("<span class='css'>");
            	out.println("	<strong>" + message + "</strong>");
            	out.println("</span>");
            	out.println("</fieldset>");
            }

        } else if(action.equals("list")) {
        	
        	Vector<Request> reqs = Request.getUserRequests(addr);
        	out.println("<!-- <VAR>" + reqs.size() + "</VAR> --->");
            //out.println("<b>Results</b>");
            //out.println("<BR>");
            //out.println("<BR>");

            long time = System.currentTimeMillis();
        	//out.println("TIME: " + time + " <BR><BR>");
            DecimalFormat df=new DecimalFormat("0.000");
            DecimalFormat dfShort=new DecimalFormat("0.0");
        	out.println("<TABLE CLASS='model-list'>");
            out.println("<TR><TH CLASS='model-list'>Model Name</TH><TH CLASS='model-list'>Model Type</TH><TH CLASS='model-list'>Params</TH><TH CLASS='model-list'>Encoding</TH><TH CLASS='model-list'>Residues</TH><TH CLASS='model-list'>Progress</TH><TH CLASS='model-list'>CC</TH><TH CLASS='model-list'>MSE</TH><TH CLASS='model-list'>Action</TH></TR>");
            for(int r=0; r<reqs.size(); r++) {
            	Request req = reqs.get(r);
                out.println("<TR><TD CLASS='model-list'>");
    			out.print( req.getModelname() );
                out.println("</TD><TD CLASS='model-list'>");
    			out.print( req.getModelType() );
                out.println("</TD><TD CLASS='model-list'>");
    			out.print( req.getModelParams() );
                out.println("</TD><TD CLASS='model-list'>");
    			out.print( req.getEncoding() );
                out.println("</TD><TD CLASS='model-list'>");
                String poss = req.getCriticalPositions();
                if(poss.length() > 10)
                	poss = poss.substring(0,10) + "...";
    			out.print( poss );
    			if(req.isResultsAvailable() ) {
                    out.println("</TD><TD CLASS='model-list'>");
                    out.println(" Complete ");
    			} else {
                    out.println("</TD><TD>");
                    
                    out.println("<div class=\"progressMeter\"> <div class=\"progressBar\" style=\"width: " + dfShort.format(req.getPercentageComplete()) +"%;\"> ");
                    //out.println("   <div style=\"padding:0px; background-color: #00BF00; width: " + dfShort.format(req.getPercentageComplete()) +"%;\">");  
        			out.print( dfShort.format( req.getPercentageComplete() ) );
                    out.println("</div> </div>");
    			}
                out.println("</TD><TD CLASS='model-list'>");
                if(req.isResultsAvailable() ) {
        			out.print( df.format( req.calculateCC() ) );
                    out.println("</TD><TD CLASS='model-list'>");
        			out.print( df.format( req.calculateMSE() ) );
                    out.println("</TD><TD CLASS='model-list'>");
                    out.println("<A href='#' onClick='loadResults(" + req.getID() + ")'>View</A>");
                    out.println("</TD></TR>");
                } else {
        			out.print( "" );
                    out.println("</TD><TD CLASS='model-list'>");
        			out.print( "" );
                    out.println("</TD><TD CLASS='model-list'>");
                    out.println("");
                    out.println("</TD></TR>");
                }

            }
            out.println("</TABLE>");	
        	
        } else if(action.equals("view")) {
        	int id = Integer.parseInt( request.getParameter("id").toString() );
        	
        	Request req = Request.getRequest(id);
        	Prediction[] preds = req.getPredictions();
        	DecimalFormat df=new DecimalFormat("0.000");
        	
        	out.println("<b>Detailed Results</b> for model <b>" + req.getModelname() + "</b>");
            out.println("<BR>");
            out.println("<BR>");
        	out.println("<TABLE CLASS='results'>");
            out.println("<TR><TH CLASS='results'>Protein Name</TH><TH CLASS='results'>Known Score</TH><TH CLASS='results'>Predicted Score</TH></TR>");
            for (int p=0; p<preds.length; p++) {

                out.println("<TR><TD>");
    			out.print( preds[p].getProteinName() );
                out.println("</TD><TD>");
    			out.print( preds[p].getTarget() );
                out.println("</TD><TD>");
    			out.print( df.format( preds[p].getPred() ) ); 
                out.println("</TD></TR>");
            }
            out.println("</TABLE>");
        	
            //out.println("<TABLE>");
            //out.println("<TR><TH>Metric</TH><TH>Results</TH></TR>");
        	//out.println("<TR><TD>Correlation Coefficient </TD><TD>" +  req.calculateCC() + "</TD></TR>");
        	//out.println("<TR><TD>Mean Squared Error </TD><TD> " + df.format( req.calculateMSE() ) + "</TD></TR>");
            //out.println("</TABLE>");
        }
        
        out.close();
	}

	
	/* OLD VERISON

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        
        String addr = request.getRemoteAddr();
        String host = request.getRemoteHost();
        
        int kernel = Integer.valueOf( request.getParameter("kernel") );
        String encoding = request.getParameter("encoding").toString();
        String modelname = request.getParameter("modelname").toString();
        String theSeqs = request.getParameter("sequences").toString();

        //System.err.println("SEQs {" + theSeqs + "}");
        
        String[] aln = theSeqs.split("~");
        System.err.println("FIRST PART {" + aln[0] + "}");
        
        String[] possies = request.getParameter("positions").toString().split("-");
        String[] efficiencies = request.getParameter("efficiencies").toString().split("~"); 
        
        int[][] positions = new int[possies.length][];
        for(int i =0; i<possies.length; i++) {
        	String[] temp = possies[i].split(",");
        	positions[i] = new int[temp.length];
        	for(int t=0; t<temp.length; t++) {
        		positions[i][t] = Integer.parseInt(temp[t]);
        	}
        }
        String[] sequences = new String[aln.length];
        String[] names = new String[aln.length];
        for(int i =0; i<aln.length; i++) {
        	String[] temp = aln[i].split(" +");
        	names[i] = temp[0].trim();
        	sequences[i] = temp[1].trim();

            System.err.println("LINE " + i + " NAME {" + names[i] + "} SEQ {" + sequences[i] + "}");
        }

        out.println("<h3>RESULTS</h3>");
        out.println("<BR>");
        org.enzymes.EnzymeEfficencyModeller modeller = new org.enzymes.EnzymeEfficencyModeller();
        modeller.xvalidate(names, sequences, efficiencies, positions, kernel, encoding);
        
    	Prediction[] preds = modeller.getPredictions();
    	
    	out.println("<TABLE>");
        out.println("<TR><TH>Protein Name</TH><TH>Known Score</TH><TH>Predicted Score</TH></TR>");
        for (int p=0; p<preds.length; p++) {

            out.println("<TR><TD>");
			out.print( preds[p].getProteinName() );
            out.println("</TD><TD>");
			out.print( preds[p].getTarget() );
            out.println("</TD><TD>");
			out.print( preds[p].getPred() ); 
            out.println("</TD></TR>");
        }
        out.println("</TABLE>");
    	
        out.println("<TABLE>");
        out.println("<TR><TH>Metric</TH><TH>Results</TH></TR>");
    	out.println("<TR><TD>Correlation Coefficient </TD><TD>" + modeller.calculateCC() + "</TD></TR>");
    	out.println("<TR><TD>Mean Squared Error </TD><TD> " + modeller.calculateMSE() + "</TD></TR>");
        out.println("</TABLE>");
        
        out.close();
	}
  
	 */
}
