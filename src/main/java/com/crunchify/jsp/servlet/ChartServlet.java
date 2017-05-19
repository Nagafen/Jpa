/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.crunchify.jsp.servlet;

import edu.co.sergio.mundo.dao.ObraDAO;
import edu.co.sergio.mundo.vo.Obra;
import java.awt.BasicStroke;
import java.awt.Color;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

public class ChartServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        	response.setContentType("image/png");
		OutputStream outputStream = response.getOutputStream();
		JFreeChart chart = getChart();
		int width = 500;
		int height = 350;
		ChartUtilities.writeChartAsPNG(outputStream, chart, width, height);

	}

	public JFreeChart getChart() {
		
                DefaultPieDataset dataset = new DefaultPieDataset();
                ObraDAO dAO=new ObraDAO();
	        //Crear la capa de servicios que se enlace con el DAO
                List<Obra> arrayList= dAO.findObraEntities();
                
                double sum = 0;
                for (int i = 0; i < arrayList.size(); i++) {
                sum=arrayList.get(i).getValor()+sum;
            }
                for (int i = 0; i < arrayList.size(); i++) {
                    if (arrayList.get(i).getValor()!=0) {
                            double porcentaje=(arrayList.get(i).getValor()/sum) *100;
                dataset.setValue(arrayList.get(i).getNombreautor().getNombreautor(), porcentaje);
                        }else{
                    double porcentaje=0;
                dataset.setValue(arrayList.get(i).getNombreautor().getNombreautor(), porcentaje);
                    }
            }
                

		boolean legend = true;
		boolean tooltips = false;
		boolean urls = false;

		JFreeChart chart = ChartFactory.createPieChart("Obras", dataset, legend, tooltips, urls);

		chart.setBorderPaint(Color.GREEN);
		chart.setBorderStroke(new BasicStroke(5.0f));
		chart.setBorderVisible(true);

		return chart;
	}

}
