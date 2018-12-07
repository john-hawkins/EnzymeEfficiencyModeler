package org.enzymes;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.biojava.bio.alignment.NeedlemanWunsch;
import org.biojava.bio.alignment.SubstitutionMatrix;
import org.biojava.bio.symbol.Alignment;
import org.biojava.bio.symbol.AlphabetManager;
import org.biojava.bio.symbol.FiniteAlphabet;
import org.biojava.bio.symbol.SymbolList;
import org.biojava.bio.symbol.SimpleSymbolList;
import org.biojava.bio.structure.io.SeqRes2AtomAligner;

import structools.structure.Residue;

/**
 * Servlet implementation class MainSearch
 */
public class AjaxTestModel extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final FiniteAlphabet alphabet = (FiniteAlphabet) AlphabetManager.alphabetForName("PROTEIN");
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AjaxTestModel() {
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
        
        String addr = request.getRemoteAddr();
        String action = request.getParameter("action").toString();
        
        //System.err.println("TEST MODEL CALLED WITH ACTION: " + action );
        
        if(action.equals("test")) {

        	int id = Integer.parseInt( request.getParameter("id").toString() );
        	int numpos = Integer.parseInt( request.getParameter("numpos").toString() );
        	String useEnsemble =  request.getParameter("useEnsemble").toString();
        	System.err.println("TEST ID=" +  id);
        	System.err.println("useEnsemble=" +  useEnsemble);
        	/*
        	for(int p=0; p<numpos; p++) {
        		String varname = "pos" + p;
        		String varval = request.getParameter(varname).toString();
        		System.err.println(varname + " = " + varval );
        	}*/
        	
        	Request req = Request.getRequest(id);
        	String refSeq = req.getRefseq();
        	int[][] posAln = req.getAlnSeqPosistions();
        	String finalSeq = refSeq;
        	int numPossies = 0;
        	for(int i=0; i<posAln.length; i++) {
            	for(int j=0; j<posAln[i].length; j++) {
                	String varname = "pos" + numPossies;
                	String varval = request.getParameter(varname).toString();
                	System.err.println("POS=" +  varname + " : " + varval);
                	finalSeq = finalSeq.substring(0,posAln[i][j]) + varval + refSeq.substring(posAln[i][j]+1);
                	numPossies++;
            	}
        	}
        	System.err.println("TEST SEQ=" +  finalSeq);
        	double output = req.test(finalSeq);
        	System.err.println("TEST RESULTZ=" +  output);
        	System.err.println("REF SEQ=" +  refSeq);
        	System.err.println("REF RESULTZ=" +  req.test(refSeq) );

            DecimalFormat df=new DecimalFormat("0.000");
        	out.println( df.format( output ) );
        	
        	/*
        	String[] paramValues = request.getParameterValues("possies");
        	for(int p=0; p<paramValues.length; p++) {
        		System.err.println("POSSIES["+p+"] = " + paramValues[p] );
        	}*/

        } else if(action.equals("test-seq")) {
        	int id = Integer.parseInt( request.getParameter("id").toString() );
        	String useEnsemble =  request.getParameter("useEnsemble").toString();
        	String testSeq =  request.getParameter("testSeq").toString();
        	Request req = Request.getRequest(id);
        	String refSeq = req.getRefseq();
        	
        	// ALL THE ALIGNING NOW HAPPENS INSIDE THE MODEL
        	System.err.println("TEST SEQ=" +  testSeq);
        	double output = req.test(testSeq);
        	System.err.println("TEST RESULTZ=" +  output);

            DecimalFormat df=new DecimalFormat("0.000");
        	out.println( df.format( output ) );
        	

        } else if(action.equals("create-model-list")) {

            String target = request.getParameter("target").toString();
        	Vector<Request> reqs = Request.getUserRequests(addr);
        	out.println("<SELECT onChange=\"ajaxRequest('AjaxTestModel?action=params&id=' + this.value, '" + target + "' );\">");
        	//out.println("<SELECT onChange=\"alert('id=' + this.value);\">");
        	out.println("<OPTION VALUE=''> SELECT A MODEL TO TEST </OPTION>");
        	for(int r=0; r<reqs.size(); r++) {
            	Request req = reqs.get(r);
                out.println("<OPTION VALUE='" + req.getID() + "'>" + req.getModelname() + ":" + req.getModelType() + "</OPTION>");
        	}
        	out.println("</SELECT>");

        } else if(action.equals("params")) {
        	
        	int id = Integer.parseInt( request.getParameter("id").toString() );

            System.err.println("      ID: " + id );
            
        	Request req = Request.getRequest(id);
        	
        	String refSeq = req.getRefseq();
        	int[][] posAln = req.getAlnSeqPosistions();
        	int[][] posOrg = req.getOriginalSeqPositions();

        	System.err.print("REF:" + refSeq);
        	String refSeqNoGaps = refSeq.replaceAll("-", "");
        	String thePossies = "";
        	String theSelects = "";
        	
        	int numPossies = 0;
        	out.println("<FORM ID='modelTestForm'>");

        	out.println("<INPUT TYPE=CHECKBOX NAME='useEnsemble' ID='useEnsemble' CHECKED onChange=\"submitModelTest();\"> Use Ensemble of XValidation Models");
      
        	out.println("<div class=\"clear\">");
        	out.println("<LABEL CLASS='LONG'>Enter New Sequence</LABEL> <INPUT TYPE=TEXT NAME='testSeq' ID='testSeq' onChange=\"submitTestSeq();\" SIZE=55>");
        	out.println("</div>");


        	for(int i=0; i<posOrg.length; i++) {
            	for(int j=0; j<posOrg[i].length; j++) {
                	System.err.print("[" + posOrg[i][j] + "] {" + posAln[i][j] + "}");
                	
                	thePossies = thePossies + posOrg[i][j] + "\n";
                	
                	String label = "<label for='blank' class='modelparam'>"+posOrg[i][j]+"</label>";
                	String theName = "pos" + numPossies;
                	String theSelector = "<SELECT NAME='possies[]' class='residues' ID='"+ theName + "' onChange=\"submitModelTest();\">";
                	theSelector = theSelector + this.getResidueOptions( refSeq.charAt(posAln[i][j]) );
                	theSelector = theSelector + "</SELECT>";
                	
                	theSelects = theSelects + theSelector + "\n";
                	
                	//out.println( label + "<SELECT NAME='possies[]' ID='"+ theName + "' onChange=\"submitModelTest();\">");
                	//this.printResidueOptions(out, refSeq.charAt(posAln[i][j]) );
                	//out.println("</SELECT>");
                	
                	numPossies++;
            	}
        	}

        	
        	/*
        	 * Now that we have all positions and the selectors
        	 * We build a string that contains the sequence with the selectors in place
        	 */
        	
        	String[] thePossiesArray = thePossies.split("\n");
        	String[] theSelectorsArray = theSelects.split("\n");
        	int[] positionsForOrder = new int[numPossies];
        	for(int i=0; i<numPossies; i++) {
        		positionsForOrder[i] = Integer.parseInt(thePossiesArray[i]);
        	}
        	// Now bubble sort them
        	for(int i=0; i<numPossies-1; i++) {
        		for(int j=i+1; j<numPossies;j++) {
        			if(positionsForOrder[i] > positionsForOrder[j]) {
        				int temp = positionsForOrder[i];
        				positionsForOrder[i] = positionsForOrder[j];
        				positionsForOrder[j] = temp;
        				
        				String tempSelect = theSelectorsArray[i];
        				theSelectorsArray[i] = theSelectorsArray[j];
        				theSelectorsArray[j] = tempSelect;
        			}
        		}
        	}
        	/*
        	 * They are sorted so now we generate the output 
        	 */
        	int startPoint = 0;
        	String display = "";
        	
        	for(int i=0; i<numPossies; i++) {
        		if(positionsForOrder[i]>(startPoint+1)) {
        			//display = display + "<label for='blank' class='residues'>"+ refSeqNoGaps.substring(startPoint, positionsForOrder[i]-1) +" </label>";
        			display = display + this.insertLabels( refSeqNoGaps.substring(startPoint, positionsForOrder[i]-1) );
        		}
        		display = display + theSelectorsArray[i];
        		startPoint = positionsForOrder[i];
        	}
        	//display = display + "<label for='blank' class='residues'>" + refSeqNoGaps.substring(startPoint, refSeqNoGaps.length() ) +" </label>";
        	display = display + this.insertLabels(refSeqNoGaps.substring(startPoint, refSeqNoGaps.length() ) );
        	
        	out.println("<div class=\"clear\">");
        	out.println("<LABEL CLASS='LONG'>Mutate Reference Sequence</LABEL>");
        	out.println("</div>");
        	out.println("<div class=\"clear\">");
        	out.println(display);
        	out.println("</div>");
        	
        	out.println("<INPUT TYPE=HIDDEN NAME='numPositions' ID='numPositions' VALUE='" + numPossies + "'>");
        	out.println("<INPUT TYPE=HIDDEN NAME='modelID' ID='modelID' VALUE='" + id + "'>");
        	out.println("</FORM>");
        	out.println("<script type=\"text/javascript\">");
        	out.println("	submitModelTest()");
        	out.println("</script>");
        	
        	
        } else if(action.equals("test")) {

        }
        
        out.close();
	}


	private String insertSpaces(String inputstring) {
		String result = "";
		for(int c=0; c<inputstring.length(); c++) {
			result = result + inputstring.charAt(c) + " ";
		}
		return result;
	}
	private String insertLabels(String inputstring) {
		String result = "";
		for(int c=0; c<inputstring.length(); c++) {
			result = result + "<label class='residue'>" + inputstring.charAt(c) + "</label>";
		}
		return result;
	}

	private void printResidueOptions(PrintWriter out, char charAt) {	
		out.println(getResidueOptions(charAt));
	}
	
	private String getResidueOptions(char charAt) {
		String result = "";
		char[] residues = Residue.getResidueCodesSingleLetter();
		for(int c=0; c<residues.length; c++) {
			if(residues[c]==charAt) {
				result = result + ("<OPTION VALUE='" + residues[c] + "' SELECTED>" + residues[c] +"</OPTION>");
			} else {
				result = result + ("<OPTION VALUE='" + residues[c] + "'>" + residues[c] +"</OPTION>");
			}
		}
		return result;
	}

	
	private static String subsMatrix =  "#  Matrix made by matblas from blosum62.iij\n" +
										"#  BLOSUM Clustered Scoring Matrix in 1/2 Bit Units\n" +
										"#  Blocks Database = /data/blocks_5.0/blocks.dat\n" +
										"#  Cluster Percentage: >= 62\n" +
										"#  Entropy =   0.6979, Expected =  -0.5209\n" +
										"   A  R  N  D  C  Q  E  G  H  I  L  K  M  F  P  S  T  W  Y  V  B  Z  X\n" +
										"A  4 -1 -2 -2  0 -1 -1  0 -2 -1 -1 -1 -1 -2 -1  1  0 -3 -2  0 -2 -1  0\n" +
										"R -1  5  0 -2 -3  1  0 -2  0 -3 -2  2 -1 -3 -2 -1 -1 -3 -2 -3 -1  0 -1\n" +
										"N -2  0  6  1 -3  0  0  0  1 -3 -3  0 -2 -3 -2  1  0 -4 -2 -3  3  0 -1\n" +
										"D -2 -2  1  6 -3  0  2 -1 -1 -3 -4 -1 -3 -3 -1  0 -1 -4 -3 -3  4  1 -1\n" +
										"C  0 -3 -3 -3  9 -3 -4 -3 -3 -1 -1 -3 -1 -2 -3 -1 -1 -2 -2 -1 -3 -3 -2\n" +
										"Q -1  1  0  0 -3  5  2 -2  0 -3 -2  1  0 -3 -1  0 -1 -2 -1 -2  0  3 -1\n" +
										"E -1  0  0  2 -4  2  5 -2  0 -3 -3  1 -2 -3 -1  0 -1 -3 -2 -2  1  4 -1\n" +
										"G  0 -2  0 -1 -3 -2 -2  6 -2 -4 -4 -2 -3 -3 -2  0 -2 -2 -3 -3 -1 -2 -1\n" +
										"H -2  0  1 -1 -3  0  0 -2  8 -3 -3 -1 -2 -1 -2 -1 -2 -2  2 -3  0  0 -1\n" +
										"I -1 -3 -3 -3 -1 -3 -3 -4 -3  4  2 -3  1  0 -3 -2 -1 -3 -1  3 -3 -3 -1\n" +
										"L -1 -2 -3 -4 -1 -2 -3 -4 -3  2  4 -2  2  0 -3 -2 -1 -2 -1  1 -4 -3 -1\n" +
										"K -1  2  0 -1 -3  1  1 -2 -1 -3 -2  5 -1 -3 -1  0 -1 -3 -2 -2  0  1 -1\n" +
										"M -1 -1 -2 -3 -1  0 -2 -3 -2  1  2 -1  5  0 -2 -1 -1 -1 -1  1 -3 -1 -1\n" +
										"F -2 -3 -3 -3 -2 -3 -3 -3 -1  0  0 -3  0  6 -4 -2 -2  1  3 -1 -3 -3 -1\n" +
										"P -1 -2 -2 -1 -3 -1 -1 -2 -2 -3 -3 -1 -2 -4  7 -1 -1 -4 -3 -2 -2 -1 -2\n" +
										"S  1 -1  1  0 -1  0  0  0 -1 -2 -2  0 -1 -2 -1  4  1 -3 -2 -2  0  0  0\n" +
										"T  0 -1  0 -1 -1 -1 -1 -2 -2 -1 -1 -1 -1 -2 -1  1  5 -2 -2  0 -1 -1  0\n" +
										"W -3 -3 -4 -4 -2 -2 -3 -2 -2 -3 -2 -3 -1  1 -4 -3 -2 11  2 -3 -4 -3 -2\n" +
										"Y -2 -2 -2 -3 -2 -1 -2 -3  2 -1 -1 -2 -1  3 -3 -2 -2  2  7 -1 -3 -2 -1\n" +
										"V  0 -3 -3 -3 -1 -2 -2 -3 -3  3  1 -2  1 -1 -2 -2  0 -3 -1  4 -3 -2 -1\n" +
										"B -2 -1  3  4 -3  0  1 -1  0 -3 -4  0 -3 -3 -2  0 -1 -4 -3 -3  4  1 -1\n" +
										"Z -1  0  0  1 -3  3  4 -2  0 -3 -3  1 -1 -3 -1  0 -1 -3 -2 -2  1  4 -1\n" +
										"X  0 -1 -1 -1 -2 -1 -1 -1 -1 -1 -1 -1 -1 -1 -2  0  0 -2 -1 -1 -1 -1 -1";
}
