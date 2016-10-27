package proyecto.com.chefappcom;

import org.json.JSONObject;

/**
 * Created by alfredo on 23/10/16.
 */

public class Platillo{

    public String nombre;
    public ListaDoble<String,String> receta;
    public ListaDoble<String,Integer> ingredientes;
    public String informacion;
    public int precio;
    public int tiempopreparacion;

    public Platillo(){

    }


    public Platillo(String nombre,ListaDoble<String,Integer> ingredientes,ListaDoble<String,String>receta,String informacion,int precio,int tiempopreparacion) {
        this.nombre=nombre;
        this.ingredientes=ingredientes;
        this.informacion=informacion;
        this.precio=precio;
        this.tiempopreparacion=tiempopreparacion;
        this.receta=receta;
    }

    public JSONObject parseJSONObject(){
        JSONObject objeto= new JSONObject();
        try {
            objeto.put("nombre", nombre);
            objeto.put("precio", precio);
            objeto.put("tiempo", tiempopreparacion);
            objeto.put("informacion", informacion);
            objeto.put("cantidad", ingredientes.size);
            int i = 1;
            for (NodoDoble<String, Integer> temp = ingredientes.head; temp != null; temp = temp.next) {
                objeto.put("ingrediente" + i, temp.llave);
                i++;
            }
            objeto.put("pasos", receta.size);
            for (NodoDoble<String, String> tmp = receta.head; tmp != null; tmp = tmp.next) {
                objeto.put(tmp.llave, tmp.valor);
            }
        }catch(Exception e){e.printStackTrace();}
        return objeto;
    }

}