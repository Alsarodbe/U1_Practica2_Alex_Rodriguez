package mx.tecnm.tepic.ladm_u1_practica2

import android.content.Context
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_main.*
import java.io.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btnsave.setOnClickListener {
            if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),0)
            }
            if(ckInterna.isChecked){
                if(guardarMemoriaInterna(edtname.text.toString())){
                    AlertDialog.Builder(this).setTitle("¡Bien! :D")
                        .setMessage("El archivo fue guardado en la memoria interna :D")
                        .setPositiveButton("OK"){d,i->d.dismiss()}
                        .show()
                }else{
                    AlertDialog.Builder(this).setTitle("Error :(")
                        .setMessage("No se ha podido guardar en la memoria interna")
                        .setPositiveButton("OK"){d,i->d.dismiss()}
                        .show()
                }
            }
            else if(ckExterna.isChecked){
                if(guardarMemoriaExterna(edtname.text.toString())){
                    AlertDialog.Builder(this).setTitle("¡Bien! :D")
                        .setMessage("El archivo fue guardado en la SD")
                        .setPositiveButton("Entendido :)"){d,i->d.dismiss()}
                        .show()
                }else{
                    AlertDialog.Builder(this).setTitle("Error :(")
                        .setMessage("No se ha podido guardar en la SD")
                        .setPositiveButton("Chale :("){d,i->d.dismiss()}
                        .show()
                }
            }
            else{
                AlertDialog.Builder(this).setTitle("Error :(")
                    .setMessage("No se ha elegido una opción >:C")
                    .setPositiveButton("Prometo elegir una opción"){d,i->d.dismiss()}
                    .show()
            }
        }

        btnload.setOnClickListener {
            if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),0)
            }
            if(ckInterna.isChecked){
                if(!leerMemoriaInterna(edtname.text.toString())){
                    AlertDialog.Builder(this).setTitle("Lo sentimos :c")
                        .setMessage("No hemos podido encontrar el archivo en memoria interna :c")
                        .setPositiveButton("Chale :c"){d,i->d.dismiss()}
                        .show()
                }
            }
            else if(ckExterna.isChecked){
                if(!leerMemoriaExterna(edtname.text.toString())) {
                    AlertDialog.Builder(this).setTitle("Lo sentimos :c")
                        .setMessage("No hemos podido encontrar el archivo en la SD")
                        .setPositiveButton("F") { d, i -> d.dismiss() }
                        .show()
                }
            }
            else{
                AlertDialog.Builder(this).setTitle("Error")
                    .setMessage("Aun no eliges que tipo de memoria quieres usar")
                    .setPositiveButton("Prometo portarme bien"){d,i->d.dismiss()}
                    .show()
            }
        }
    }
    private fun guardarMemoriaInterna(filename:String) : Boolean{
        try{
            var flujoSalida = OutputStreamWriter(openFileOutput(filename, Context.MODE_PRIVATE))
            var data = entrada.text.toString()
            flujoSalida.write(data)
            flujoSalida.flush()
            flujoSalida.close()
            entrada.setText("")
        }catch (io: IOException){
            return false
        }
        return true
    }
    private fun leerMemoriaInterna(filename: String) : Boolean{
        try{
            var flujoEntrada = BufferedReader(InputStreamReader(openFileInput(filename)))
            var data = flujoEntrada.readLine()
            entrada.setText(data)
            flujoEntrada.close()
        }catch (io:IOException){
            return false
        }
        return true
    }
    private fun guardarMemoriaExterna(filename: String) : Boolean{
        try {
            if (Environment.getExternalStorageState() != Environment.MEDIA_MOUNTED) {
                AlertDialog.Builder(this).setTitle("Algo va mal...")
                    .setMessage("No hemos detectado ninguna SD montada")
                    .setPositiveButton("Es verdad, no tengo la SD puesta D:") { d, i -> d.dismiss() }
                    .show()
                return false
            }
            var rutaSD = Environment.getExternalStorageDirectory()
            var dirSD = File(rutaSD.absolutePath, filename)
            var flujoSalida = OutputStreamWriter(FileOutputStream(dirSD))
            flujoSalida.write(entrada.text.toString())
            flujoSalida.flush()
            flujoSalida.close()
            entrada.setText("")
        } catch (io:IOException) {
            return false
        }
        return true
    }

    private fun leerMemoriaExterna(filename: String) : Boolean{
        try {
            if (Environment.getExternalStorageState() != Environment.MEDIA_MOUNTED) {
                AlertDialog.Builder(this).setTitle("Algo va mal...")
                    .setMessage("No hemos detectado ninguna SD montada")
                    .setPositiveButton("Que mal servicio") { d, i -> d.dismiss() }
                    .show()
                return false
            }
            var rutaSD = Environment.getExternalStorageDirectory()
            var flujo = File(rutaSD.absolutePath, filename)
            var flujoEntrada = BufferedReader(InputStreamReader(FileInputStream(flujo)))
            var data = flujoEntrada.readLine()
            entrada.setText(data)
            flujoEntrada.close()
        } catch (io: IOException) {
            return false
        }
        return true
    }
}