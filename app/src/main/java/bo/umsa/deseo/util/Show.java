package bo.umsa.deseo.util;

import java.util.ArrayList;
import java.util.Calendar;

public class Show {


    private static ArrayList<Show> ShowArrayList;

    private int id;
    private String name;

    public Show(int id, String name) {
        this.id = id;
        this.name = name;

    }

    public static void initShow()
    {
        ShowArrayList = new ArrayList<>();
        Calendar c = Calendar.getInstance();
        //c.set(año,mes,dia) // vairables int
        int dia =  c.get(Calendar.DAY_OF_WEEK);
        if(dia==Calendar.SUNDAY){
            ShowArrayList.add(new Show(0,"07:00 – Música"));
            ShowArrayList.add(new Show(1,"07:30 – Novilunio"));
            ShowArrayList.add(new Show(2,"08:30 – Música"));
            ShowArrayList.add(new Show(3,"09:30 – Luche, Pare de Sufrir"));
            ShowArrayList.add(new Show(4,"11:30 – Canasta de combate"));
            ShowArrayList.add(new Show(5,"12:30 – La vuelta al mundo en 60 minutos"));
            ShowArrayList.add(new Show(6,"13:30 – Música"));
            ShowArrayList.add(new Show(7,"16:00 – Música"));
            ShowArrayList.add(new Show(8,"17:00 – Luche, Pare de Sufrir (Reprís)"));
            ShowArrayList.add(new Show(9,"19:00 – La vuelta al mundo en 60 minutos"));
            ShowArrayList.add(new Show(10,"20:00 – Plenilunio"));
            ShowArrayList.add(new Show(11,"21:00 – La Beatleoteca"));
            ShowArrayList.add(new Show(12,"22:00 – Música"));
            ShowArrayList.add(new Show(13,"23:00 – Música"));
            ShowArrayList.add(new Show(14,"24:00 – Cierre de emisión"));
        }if(dia==Calendar.MONDAY){
        ShowArrayList.add(new Show(0,"07:00 – Música"));
        ShowArrayList.add(new Show(1,"07:30 – Novilunio"));
        ShowArrayList.add(new Show(2,"08:30 – Música"));
        ShowArrayList.add(new Show(3,"09:30 – Luche, Pare de Sufrir"));
        ShowArrayList.add(new Show(4,"11:30 – Canasta de combate"));
        ShowArrayList.add(new Show(5,"12:30 – La vuelta al mundo en 60 minutos"));
        ShowArrayList.add(new Show(6,"13:30 – Música"));
        ShowArrayList.add(new Show(7,"16:00 – Música"));
        ShowArrayList.add(new Show(8,"17:00 – Luche, Pare de Sufrir (Reprís)"));
        ShowArrayList.add(new Show(9,"19:00 – La vuelta al mundo en 60 minutos"));
        ShowArrayList.add(new Show(10,"20:00 – Plenilunio"));
        ShowArrayList.add(new Show(11,"21:00 – La Beatleoteca"));
        ShowArrayList.add(new Show(12,"22:00 – Música"));
        ShowArrayList.add(new Show(13,"23:00 – Música"));
        ShowArrayList.add(new Show(14,"24:00 – Cierre de emisión"));
        }
        if(dia==Calendar.TUESDAY){
            ShowArrayList.add(new Show(0,"07:00 – Música"));
            ShowArrayList.add(new Show(1,"07:30 – Novilunio"));
            ShowArrayList.add(new Show(2,"08:30 – Música"));
            ShowArrayList.add(new Show(3,"09:30 – Luche, Pare de Sufrir"));
            ShowArrayList.add(new Show(4,"11:30 – Canasta de combate"));
            ShowArrayList.add(new Show(5,"12:30 – La vuelta al mundo en 60 minutos"));
            ShowArrayList.add(new Show(6,"13:30 – Música"));
            ShowArrayList.add(new Show(7,"16:00 – Música"));
            ShowArrayList.add(new Show(8,"17:00 – Luche, Pare de Sufrir (Reprís)"));
            ShowArrayList.add(new Show(9,"19:00 – La vuelta al mundo en 60 minutos"));
            ShowArrayList.add(new Show(10,"20:00 – Plenilunio"));
            ShowArrayList.add(new Show(11,"21:00 – La Beatleoteca"));
            ShowArrayList.add(new Show(12,"22:00 – Música"));
            ShowArrayList.add(new Show(13,"23:00 – Música"));
            ShowArrayList.add(new Show(14,"24:00 – Cierre de emisión"));
        }
        if(dia==Calendar.WEDNESDAY){
            ShowArrayList.add(new Show(0,"07:00 – Música"));
            ShowArrayList.add(new Show(1,"07:30 – Novilunio"));
            ShowArrayList.add(new Show(2,"08:30 – Música"));
            ShowArrayList.add(new Show(3,"09:30 – Luche, Pare de Sufrir"));
            ShowArrayList.add(new Show(4,"11:30 – Canasta de combate"));
            ShowArrayList.add(new Show(5,"12:30 – La vuelta al mundo en 60 minutos"));
            ShowArrayList.add(new Show(6,"13:30 – Música"));
            ShowArrayList.add(new Show(7,"16:00 – Música"));
            ShowArrayList.add(new Show(8,"17:00 – Luche, Pare de Sufrir (Reprís)"));
            ShowArrayList.add(new Show(9,"19:00 – La vuelta al mundo en 60 minutos"));
            ShowArrayList.add(new Show(10,"20:00 – Plenilunio"));
            ShowArrayList.add(new Show(11,"21:00 – La Beatleoteca"));
            ShowArrayList.add(new Show(12,"22:00 – Música"));
            ShowArrayList.add(new Show(13,"23:00 – Música"));
            ShowArrayList.add(new Show(14,"24:00 – Cierre de emisión"));
        }
        if(dia==Calendar.THURSDAY){
            ShowArrayList.add(new Show(0,"07:00 – Música"));
            ShowArrayList.add(new Show(1,"07:30 – Novilunio"));
            ShowArrayList.add(new Show(2,"08:30 – Música"));
            ShowArrayList.add(new Show(3,"09:30 – Luche, Pare de Sufrir"));
            ShowArrayList.add(new Show(4,"11:30 – Canasta de combate"));
            ShowArrayList.add(new Show(5,"12:30 – La vuelta al mundo en 60 minutos"));
            ShowArrayList.add(new Show(6,"13:30 – Música"));
            ShowArrayList.add(new Show(7,"16:00 – Música"));
            ShowArrayList.add(new Show(8,"17:00 – Luche, Pare de Sufrir (Reprís)"));
            ShowArrayList.add(new Show(9,"19:00 – La vuelta al mundo en 60 minutos"));
            ShowArrayList.add(new Show(10,"20:00 – Plenilunio"));
            ShowArrayList.add(new Show(11,"21:00 – La Beatleoteca"));
            ShowArrayList.add(new Show(12,"22:00 – Música"));
            ShowArrayList.add(new Show(13,"23:00 – Música"));
            ShowArrayList.add(new Show(14,"24:00 – Cierre de emisión"));
        }
        if(dia==Calendar.FRIDAY){
            ShowArrayList.add(new Show(0,"07:00 – Música"));
            ShowArrayList.add(new Show(1,"07:30 – Novilunio"));
            ShowArrayList.add(new Show(2,"08:30 – Música"));
            ShowArrayList.add(new Show(3,"09:30 – Luche, Pare de Sufrir"));
            ShowArrayList.add(new Show(4,"11:30 – Canasta de combate"));
            ShowArrayList.add(new Show(5,"12:30 – La vuelta al mundo en 60 minutos"));
            ShowArrayList.add(new Show(6,"13:30 – Música"));
            ShowArrayList.add(new Show(7,"16:00 – Música"));
            ShowArrayList.add(new Show(8,"17:00 – Luche, Pare de Sufrir (Reprís)"));
            ShowArrayList.add(new Show(9,"19:00 – La vuelta al mundo en 60 minutos"));
            ShowArrayList.add(new Show(10,"20:00 – Plenilunio"));
            ShowArrayList.add(new Show(11,"21:00 – La Beatleoteca"));
            ShowArrayList.add(new Show(12,"22:00 – Música"));
            ShowArrayList.add(new Show(13,"23:00 – Música"));
            ShowArrayList.add(new Show(14,"24:00 – Cierre de emisión"));
        }
        if(dia==Calendar.SATURDAY){
            ShowArrayList.add(new Show(0,"07:00 – Música"));
            ShowArrayList.add(new Show(1,"07:30 – Novilunio"));
            ShowArrayList.add(new Show(2,"08:30 – Música"));
            ShowArrayList.add(new Show(3,"09:30 – Luche, Pare de Sufrir"));
            ShowArrayList.add(new Show(4,"11:30 – Canasta de combate"));
            ShowArrayList.add(new Show(5,"12:30 – La vuelta al mundo en 60 minutos"));
            ShowArrayList.add(new Show(6,"13:30 – Música"));
            ShowArrayList.add(new Show(7,"16:00 – Música"));
            ShowArrayList.add(new Show(8,"17:00 – Luche, Pare de Sufrir (Reprís)"));
            ShowArrayList.add(new Show(9,"19:00 – La vuelta al mundo en 60 minutos"));
            ShowArrayList.add(new Show(10,"20:00 – Plenilunio"));
            ShowArrayList.add(new Show(11,"21:00 – La Beatleoteca"));
            ShowArrayList.add(new Show(12,"22:00 – Música"));
            ShowArrayList.add(new Show(13,"23:00 – Música"));
            ShowArrayList.add(new Show(14,"24:00 – Cierre de emisión"));
        }

    }

    public static ArrayList<Show> getShowArrayList() {
        return ShowArrayList;
    }

    public static String[] showNames(){
        String[] names = new String[ShowArrayList.size()];
        for (int i = 0; i <ShowArrayList.size() ; i++) {
            names[i]=ShowArrayList.get(i).name;
        }
        return names;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
