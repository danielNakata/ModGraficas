/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package t3b.graficas.main.grf;

import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
import org.jfree.chart.axis.DateTickUnit;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;

/**
 *
 * @author daniel
 */
public class GLinea {
    
    private static final int GRAF_FECHA = 1;
    private static final int GRAF_CATEGO = 2;

    private Object dataset = null;
    private JFreeChart chart = null;
    private ChartPanel chartPanel = null;
    private ArrayList<Object> datos = null;
    private ArrayList<String> series = null;
    private int tipoGraf  = 0;
    private String titulo = "";
    private String ejex   = "";
    private String ejey   = "";
    
    /**
     * Constructor para hacer una grafica de linea
     * @param titulo titulo para la grafica
     * @param ejex etiqueta para el ejex
     * @param ejey etiqueta para el ejey
     * @param series lista con las series que tendra la grafica 
     * @param datos los datos a graficar
     * @param tipoGraf tipo de grafica 1 si se requiere fechas
     */
    public GLinea(String titulo
                ,String ejex
                ,String ejey
                ,ArrayList<String> series
                ,ArrayList<Object> datos
                ,int tipoGraf){
        this.titulo = titulo;
        this.ejex = ejex;
        this.ejey = ejey;
        this.series = series;
        this.datos = datos;
        this.tipoGraf = tipoGraf;
        this.cargaDatos();
        this.chart = (this.tipoGraf == 1?this.creaXYGrafica():this.creaGrafica());
        this.chartPanel = this.creaPanel();
    }
    
    
    private ChartPanel creaPanel(){
        ChartPanel panel = new ChartPanel(this.chart);
        panel.setPreferredSize(new java.awt.Dimension(500, 500));
        panel.setVisible(true);
        panel.setDomainZoomable(true);
        return panel;
    }
    
    public Object obtienePanel(){
        return this.chartPanel;
    }
    
    
    /**
     * Genera el objeto de la grafica
     * @return 
     */
    private JFreeChart creaXYGrafica(){
        JFreeChart grafica = null;
        try{
            grafica = ChartFactory.createXYLineChart(this.titulo
                    , this.ejex
                    , this.ejey
                    , (XYDataset)this.dataset
                    , PlotOrientation.VERTICAL, true, true, false);
            grafica.setBorderPaint(Color.white);
            XYPlot plot = (XYPlot) grafica.getPlot();
            DateAxis dateAxis = new DateAxis();
            dateAxis.setDateFormatOverride(new SimpleDateFormat("yyyy-MM-dd"));
            dateAxis.setLabelAngle(-90);
            dateAxis.setTickUnit(new DateTickUnit(DateTickUnit.DAY, 1, new SimpleDateFormat("yyyy-MM-dd")));
            dateAxis.setVerticalTickLabels(true);
            
            plot.setDomainAxis(dateAxis);
        }catch(Exception ex){
            System.out.println("Excepcion en creaGrafica: " + ex);
            grafica = null;
        }
        return grafica;
    }
    
    
    private JFreeChart creaGrafica(){
        JFreeChart grafica = null;
        try{
            grafica = ChartFactory.createLineChart(this.titulo
                    , this.ejex
                    , this.ejey
                    , (CategoryDataset)this.dataset
                    , PlotOrientation.VERTICAL, true, true, false);
            grafica.setBorderPaint(Color.white);
            CategoryPlot plot = (CategoryPlot) grafica.getPlot();
            plot.setBackgroundPaint(Color.lightGray);
            plot.setRangeGridlinePaint(Color.white);
            NumberAxis rangeAxis = (NumberAxis)plot.getRangeAxis();
            rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
            rangeAxis.setAutoRangeIncludesZero(true);
            CategoryAxis domainAxis = plot.getDomainAxis();
            domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_90);
            
        }catch(Exception ex){
            System.out.println("Excepcion en creaGrafica: " + ex);
            grafica = null;
        }
        return grafica;
    }
    
    
    /**
     * Metodo para cargar los Datos de la grafica
     */
    public void cargaDatos(){
        if((this.datos != null)&&(this.series != null)){
            switch(this.tipoGraf){
                case GRAF_FECHA:
                    this.dataset = (XYDataset)this.crearSerieTiempo();
                    break;
                default:
                    this.dataset = (CategoryDataset) this.creaSerieNormal();
                    break;
            }
        }
    }
    
    /**
     * MEtodo para crear las series, de acuerdo a lo que se indique en el constructor
     * @return 
     */
    private Object crearSerieTiempo(){
        TimeSeriesCollection seriedatos = new TimeSeriesCollection();
        try{
            int idxseries = this.series.size();
            for(int i = 0; i < idxseries; i++){
                TimeSeries serie = new TimeSeries(this.series.get(i));
                LinkedHashMap datosseries = (LinkedHashMap) this.datos.get(i);
                Set llaves = datosseries.keySet();
                Iterator it = llaves.iterator();
                while(it.hasNext()){
                    String llave = it.next().toString();
                    serie.add(new Day(this.obtieneFecha(llave))
                            , (Double)datosseries.get(llave));
                }
                seriedatos.addSeries(serie);
            }            
        }catch(Exception ex){
            System.out.println("Excepcion crearSerieTiempo: " + ex);
            seriedatos = null;
        }
        return seriedatos;
    }
    
    
    private Object creaSerieNormal(){
        DefaultCategoryDataset seriedatos = new DefaultCategoryDataset();
        try{
            int idxseries = this.series.size();
            for(int i = 0; i < idxseries; i++){
                LinkedHashMap datosseries = (LinkedHashMap) this.datos.get(i);
                Set llaves = datosseries.keySet();
                Iterator it = llaves.iterator();
                while(it.hasNext()){
                    String llave = it.next().toString();
                    seriedatos.addValue((Double)datosseries.get(llave)
                            , this.series.get(i), llave);
                }
            }            
        }catch(Exception ex){
            System.out.println("Excepcion crearSerieTiempo: " + ex);
            seriedatos = null;
        }
        return seriedatos;
    }
    
    
    /**
     * Obtiene la fecha indicada
     * @param aux
     * @return 
     */
    private Date obtieneFecha(String aux){
        Date fecha = new Date();
        Calendar cal = Calendar.getInstance();
        try{
            cal.set(Integer.parseInt(aux.split("-")[0]), ((Integer.parseInt(aux.split("-")[1]))-1), Integer.parseInt(aux.split("-")[2]),0, 0, 0);
            fecha = cal.getTime();
        }catch(Exception ex){
            System.out.println("Excepcion obtieneFecha: " + ex);
            fecha = new Date();
        }        
        return fecha;
    }

    
}
