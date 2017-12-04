package catmaerkan.com.notsepetiapp.data;

/**
 * Created by Erkan ÇATMA on 12.10.2017.
 */

public class Notlar {

    private int id;
    private String notIcerik;
    private long notEklenmetarihi;
    private long notTarih;
    private int tamamlandi;

    public Notlar() {

    }

    public Notlar(String notIcerik) {
        this.notIcerik = notIcerik;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNotIcerik() {
        return notIcerik;
    }

    public void setNotIcerik(String notIcerik) {
        this.notIcerik = notIcerik;
    }

    public long getNotTarih() {
        return notTarih;
    }

    public long getNotEklenmetarihi() {
        return notEklenmetarihi;
    }

    public void setNotEklenmetarihi(long notEklenmetarihi) {
        this.notEklenmetarihi = notEklenmetarihi;
    }

    public void setNotTarih(long notTarih) {
        this.notTarih = notTarih;
    }

    public int getTamamlandi() {
        return tamamlandi;
    }

    public void setTamamlandi(int tamamlandi) {
        this.tamamlandi = tamamlandi;
    }
}
