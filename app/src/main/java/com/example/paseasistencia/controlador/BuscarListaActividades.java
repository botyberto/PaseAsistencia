package com.example.paseasistencia.controlador;

import android.os.AsyncTask;
import android.util.Log;

import com.afollestad.bridge.Bridge;
import com.afollestad.bridge.BridgeException;
import com.afollestad.bridge.Request;
import com.afollestad.bridge.Response;
import com.example.paseasistencia.R;
import com.example.paseasistencia.complementos.Complementos;
import com.example.paseasistencia.model.Actividades;
import com.example.paseasistencia.model.Mallas;
import com.example.paseasistencia.model.Puestos;
import com.example.paseasistencia.model.TiposPermisos;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public class BuscarListaActividades extends AsyncTask<Void, Integer, String> {
    private Controlador controlador;
    private IactualizacionDatos iactualizacionDatos;
    private String servidor;

    public BuscarListaActividades(IactualizacionDatos iactualizacionDatos, Controlador controlador) {
        this.controlador = controlador;
        this.iactualizacionDatos = iactualizacionDatos;

        this.servidor = this.controlador.getConfiguracion().getUrl();
        if(!this.servidor.endsWith("/"))
            this.servidor = this.servidor+"/";
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        iactualizacionDatos.iniciarAnimacion(0, 0);
        iactualizacionDatos.actualizacionMensajes(Controlador.getCONTEXT().getString(R.string.msn_inicio));
    }

    @Override
    protected void onPostExecute(String mensaje) {
        super.onPostExecute(mensaje);
        iactualizacionDatos.actualizacionMensajes(mensaje);
        iactualizacionDatos.finalizarAnimacion();
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected String doInBackground(Void... voids) {
        String respuesta = null;

        respuesta = buscarListaPuestos();
        if(!respuesta.equals(Controlador.getCONTEXT().getString(R.string.msn_fin))||!respuesta.equals(Controlador.getCONTEXT().getString(R.string.msn_lista_vacios))){
            respuesta = buscarListaActividades();
            if(!respuesta.equals(Controlador.getCONTEXT().getString(R.string.msn_fin))||!respuesta.equals(Controlador.getCONTEXT().getString(R.string.msn_lista_vacios))){
                respuesta = buscarListaMallas();
                if(!respuesta.equals(Controlador.getCONTEXT().getString(R.string.msn_fin))||!respuesta.equals(Controlador.getCONTEXT().getString(R.string.msn_lista_vacios))){
                    respuesta = buscarListaPermisos();
                    if(!respuesta.equals(Controlador.getCONTEXT().getString(R.string.msn_fin))||!respuesta.equals(Controlador.getCONTEXT().getString(R.string.msn_lista_vacios))){
                        respuesta = buscarListaTrabajadores();
                    }
                }
            }
        }

        Log.i("PETICION","<+++++++++++++++++BUSQUEDA DE PUESTOS FINALIZADA+++++++++++++++++> ");
        return respuesta;
    }

    private String buscarListaPuestos(){
        String respuesta=null;
        try {
            String url = this.servidor+"ListaPuestoes";
            Log.i("PETICION","Inicia peticion de puestos ");

            try{
                Request request = Bridge.get(url).throwIfNotSuccess().request();
                Response response = request.response();
                List<Puestos>listaPuestos = response.asClassList(Puestos.class);

                if(listaPuestos.size() == 0){
                    respuesta = Controlador.getCONTEXT().getString(R.string.msn_lista_vacios);
                    Log.i("ERROR",Controlador.getCONTEXT().getString(R.string.msn_lista_vacios));
                }else if (listaPuestos.size() > 0){
                    controlador.reiniciarListaPuestos();
                    for (Puestos puesto : listaPuestos) {
                        controlador.setPuetos(puesto);
                    }
                    respuesta = Controlador.getCONTEXT().getString(R.string.msn_fin);
                }else{
                    respuesta = Controlador.getCONTEXT().getString(R.string.msn_enesperado);
                    Log.i("ERROR",Controlador.getCONTEXT().getString(R.string.msn_enesperado));
                }

                Log.i("PETICION","termina peticion de mallas, Total mallas "+listaPuestos.size());
            }catch(BridgeException e){
                respuesta = Controlador.getCONTEXT().getString(R.string.msn_sinConexion);
                Log.i("PETICION","Error al conectar con el servidor "+ e.getMessage());
            }
        }catch (RuntimeException ee){
            respuesta = Controlador.getCONTEXT().getString(R.string.msn_enesperado);
            Log.i("PETICION",ee.getMessage());

        }catch (Exception e){
            respuesta = Controlador.getCONTEXT().getString(R.string.msn_enesperado);
            Log.i("PETICION",e.getMessage());
        }

        return  respuesta;
    }

    private String buscarListaActividades(){
        String respuesta=null;
        try {
            String url = this.servidor+"ACTIVIDADEs";

            Log.i("PETICION","Inicia peticion de Actividades "+url);

            try{
                Request request = Bridge.get(url).throwIfNotSuccess().request();
                Response response = request.response();
                List<Actividades>listaActividades = response.asClassList(Actividades.class);

                if(listaActividades.size() == 0){
                    respuesta = Controlador.getCONTEXT().getString(R.string.msn_lista_vacios);
                    Log.i("ERROR",Controlador.getCONTEXT().getString(R.string.msn_lista_vacios));
                }else if (listaActividades.size() > 0){
                    controlador.reiniciarListaActividades();
                    for (Actividades actividad : listaActividades) {
                        controlador.setActividad(actividad);
                    }
                    respuesta = Controlador.getCONTEXT().getString(R.string.msn_fin);
                }else{
                    respuesta = Controlador.getCONTEXT().getString(R.string.msn_enesperado);
                    Log.i("ERROR",Controlador.getCONTEXT().getString(R.string.msn_enesperado));
                }

                Log.i("PETICION","termina peticion de mallas, Total mallas "+listaActividades.size());
            }catch(BridgeException e){
                respuesta = Controlador.getCONTEXT().getString(R.string.msn_sinConexion);
                Log.i("PETICION","Error al conectar con el servidor "+ e.getMessage());
            }
        }catch (RuntimeException ee){
            respuesta = Controlador.getCONTEXT().getString(R.string.msn_enesperado);
            Log.i("PETICION",ee.getMessage());

        }catch (Exception e){
            respuesta = Controlador.getCONTEXT().getString(R.string.msn_enesperado);
            Log.i("PETICION",e.getMessage());
        }

        return  respuesta;
    }

    private String buscarListaMallas(){
        String respuesta=null;
        try {
            String url = this.servidor+"mallas";
            Log.i("PETICION","Inicia peticion de mallas ");

            try{
                Request request = Bridge.get(url).throwIfNotSuccess().request();
                Response response = request.response();
                List<Mallas>listaMallas = response.asClassList(Mallas.class);

                if(listaMallas.size() == 0){
                    respuesta = Controlador.getCONTEXT().getString(R.string.msn_lista_vacios);
                    Log.i("ERROR",Controlador.getCONTEXT().getString(R.string.msn_lista_vacios));
                }else if (listaMallas.size() > 0){
                    controlador.reiniciarListaMallas();
                    for (Mallas mallas : listaMallas) {
                        controlador.setMallas(mallas);
                    }
                    respuesta = Controlador.getCONTEXT().getString(R.string.msn_fin);
                }else{
                    respuesta = Controlador.getCONTEXT().getString(R.string.msn_enesperado);
                    Log.i("ERROR",Controlador.getCONTEXT().getString(R.string.msn_enesperado));
                }

                Log.i("PETICION","termina peticion de mallas, Total mallas "+listaMallas.size());
            }catch(BridgeException e){
                respuesta = Controlador.getCONTEXT().getString(R.string.msn_sinConexion);
                Log.i("PETICION","Error al conectar con el servidor "+ e.getMessage());
            }
        }catch (RuntimeException ee){
            respuesta = Controlador.getCONTEXT().getString(R.string.msn_enesperado);
            Log.i("PETICION",ee.getMessage());

        }catch (Exception e){
            respuesta = Controlador.getCONTEXT().getString(R.string.msn_enesperado);
            Log.i("PETICION",e.getMessage());
        }

        return  respuesta;
    }

    private String buscarListaPermisos(){
        String respuesta=null;
        try {
            String url = this.servidor+"tiposPermisoes";
            Log.i("PETICION","Inicia peticion de tipos de permisos ");

            try{
                Request request = Bridge.get(url).throwIfNotSuccess().request();
                Response response = request.response();
                List<TiposPermisos>listaPermisos = response.asClassList(TiposPermisos.class);

                if(listaPermisos.size() == 0){
                    respuesta = Controlador.getCONTEXT().getString(R.string.msn_lista_vacios);
                    Log.i("ERROR",Controlador.getCONTEXT().getString(R.string.msn_lista_vacios));
                }else if (listaPermisos.size() > 0){
                    controlador.reiniciarListaTiposPermisos();
                    for (TiposPermisos permisos : listaPermisos) {
                        controlador.setTipoPermisos(permisos);
                    }
                    respuesta = Controlador.getCONTEXT().getString(R.string.msn_fin);
                }else{
                    respuesta = Controlador.getCONTEXT().getString(R.string.msn_enesperado);
                    Log.i("ERROR",Controlador.getCONTEXT().getString(R.string.msn_enesperado));
                }

                Log.i("PETICION","termina peticion de mallas, Total mallas "+listaPermisos.size());
            }catch(BridgeException e){
                respuesta = Controlador.getCONTEXT().getString(R.string.msn_sinConexion);
                Log.i("PETICION","Error al conectar con el servidor "+ e.getMessage());
            }
        }catch (RuntimeException ee){
            respuesta = Controlador.getCONTEXT().getString(R.string.msn_enesperado);
            Log.i("PETICION",ee.getMessage());

        }catch (Exception e){
            respuesta = Controlador.getCONTEXT().getString(R.string.msn_enesperado);
            Log.i("PETICION",e.getMessage());
        }

        return  respuesta;
    }

    private String buscarListaTrabajadores(){
        String respuesta=null;
        try {
            String url = this.servidor+"TRABAJADOREs?fechaInicioSem="+ Complementos.getDateActualToStringServidor();
            Log.i("PETICION","Inicia peticion de tipos de permisos ");

            try{
                Request request = Bridge.get(url).throwIfNotSuccess().request();
                Response response = request.response();

                JSONArray jsonArray = response.asJsonArray();
                Log.i("json",jsonArray.toString());
                List<TiposPermisos>listaPermisos = response.asClassList(TiposPermisos.class);

                if(jsonArray.length() == 0){
                    respuesta = Controlador.getCONTEXT().getString(R.string.msn_lista_vacios);
                    Log.i("ERROR",Controlador.getCONTEXT().getString(R.string.msn_lista_vacios));
                }else if (jsonArray.length() > 0){
                    controlador.reiniciarListaTrabajadores();

                    for (int i=0;i<jsonArray.length();i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Integer clave =(Integer) jsonObject.get("Clave");
                        Integer consecutivo =(Integer) jsonObject.get("Consecutivo");
                        String nombre =(String) jsonObject.get("Nombre");
                        Integer numero =(Integer) jsonObject.get("Numero");
                        String puesto =(String) jsonObject.get("Puesto");

                        controlador.setTrabajadores(clave,consecutivo,nombre,numero,puesto);
                    }
                    respuesta = Controlador.getCONTEXT().getString(R.string.msn_fin);
                }else{
                    respuesta = Controlador.getCONTEXT().getString(R.string.msn_enesperado);
                    Log.i("ERROR",Controlador.getCONTEXT().getString(R.string.msn_enesperado));
                }

                Log.i("PETICION","termina peticion de mallas, Total mallas "+listaPermisos.size());
            }catch(BridgeException e){
                respuesta = Controlador.getCONTEXT().getString(R.string.msn_sinConexion);
                Log.i("PETICION","Error al conectar con el servidor "+ e.getMessage());
            }
        }catch (RuntimeException ee){
            respuesta = Controlador.getCONTEXT().getString(R.string.msn_enesperado);
            Log.i("PETICION",ee.getMessage()+"");

        }catch (Exception e){
            respuesta = Controlador.getCONTEXT().getString(R.string.msn_enesperado);
            Log.i("PETICION",e.getMessage());
        }

        return  respuesta;
    }

}
