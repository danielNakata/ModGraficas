/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package t3b.graficas.main.grf;

import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

/**
 *
 * @author daniel
 */
public class GBarra {
    
    private DefaultCategoryDataset dataset = null;
    private JFreeChart chart = null;
    private ChartPanel chartPanel = null;
    private ArrayList<Object> datos = null;
    private ArrayList<String> series = null;
    private int tipoGraf  = 0;
    private String titulo = "";
    private String ejex   = "";
    private String ejey   = "";
    
    public GBarra(String titulo, String ejex, String ejey, ArrayList<String> series, ArrayList<Object> datos){
        this.titulo = titulo;
        this.ejex = ejex;
        this.ejey = ejey;
        this.series = series;
        this.datos = datos;
        this.cargaDatos();
        this.chart = this.creaGrafica();
        this.chartPanel = this.creaPanel();
    }
    
    private ChartPanel creaPanel(){
        ChartPanel panel = new ChartPanel(this.chart);
        panel.setPreferredSize(new java.awt.Dimension(500, 500));
        panel.setVisible(true);
        panel.setDomainZoomable(true);
        panel.setRangeZoomable(true);
        return panel;
    }
    
    public ChartPanel obtienePanel(){
        return this.chartPanel;
    }
    
    
    private JFreeChart creaGrafica(){
        JFreeChart grafica = null;
        try{
            grafica = ChartFactory.createBarChart(this.titulo
                    , this.ejex
                    , this.ejey
                    , this.dataset
                    , PlotOrientation.VERTICAL, true, true, false);
            grafica.setBorderPaint(Color.white);
            CategoryPlot plot = grafica.getCategoryPlot();
            plot.setBackgroundPaint(Color.lightGray);
            plot.setDomainGridlinePaint(Color.white);
            plot.setRangeGridlinePaint(Color.lightGray);
            
            NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
            rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
            
            BarRenderer renderer = (BarRenderer) plot.getRenderer();
            renderer.setDrawBarOutline(false);
            
            CategoryAxis domainAxis = plot.getDomainAxis();
            domainAxis.setCategoryLabelPositions(CategoryLabelPositions.createUpRotationLabelPositions(Math.PI/2.0));
        }catch(Exception ex){
            System.out.println("Excepcion en creaGrafica: " + ex);
            grafica = null;
        }
        return grafica;
    }
    
    
    public void cargaDatos(){
        if((this.datos != null)&&(this.series != null)){
            this.dataset = this.creaSerie();
        }
    }
    
    
    private DefaultCategoryDataset creaSerie(){
        DefaultCategoryDataset datos = new DefaultCategoryDataset();
        try{
            int idxseries = this.series.size();
            for(int i = 0; i < idxseries; i++){
                LinkedHashMap datosseries = (LinkedHashMap) this.datos.get(i);
                Set llaves = datosseries.keySet();
                Iterator it = llaves.iterator();
                while(it.hasNext()){
                    String llave = it.next().toString();
                    datos.addValue((Double)datosseries.get(llave)
                            ,this.series.get(i)
                            ,llave );
                }
            }
        }catch(Exception ex){
            System.out.println("Excepcion creaSerie: " + ex);
            datos = null;                    
        }
        return datos;
    }
}
