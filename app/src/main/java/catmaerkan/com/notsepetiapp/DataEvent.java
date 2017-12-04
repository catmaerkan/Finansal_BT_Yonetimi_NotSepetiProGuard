package catmaerkan.com.notsepetiapp;

/**
 * Created by Erkan ÇATMA on 16.10.2017.
 */

public class DataEvent {

    public static class NotEkleDialogGoster{

        private int tetikle;

        public int getTetikle() {
            return tetikle;
        }

        public void setTetikle(int tetikle) {
            this.tetikle = tetikle;
        }

        public NotEkleDialogGoster(int tetikle) {
            this.tetikle = tetikle;
        }
    }


    public static class DataGuncelleMethoduTetikle{

        private int tetikle;

        public int getTetikle() {
            return tetikle;
        }

        public void setTetikle(int tetikle) {
            this.tetikle = tetikle;
        }

        public DataGuncelleMethoduTetikle(int tetikle) {
            this.tetikle = tetikle;
        }
    }

    public static class KaydirilanNotunPozisyonu{

        private int position;

        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
        }

        public KaydirilanNotunPozisyonu(int position) {
            this.position = position;
        }
    }

    public static class DialogTamamlaNotPosition{

        private int position;

        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
        }

        public DialogTamamlaNotPosition(int position) {
            this.position = position;
        }
    }

    public static class TamamlanacakNotPosition{

        private int position;

        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
        }

        public TamamlanacakNotPosition(int position) {
            this.position = position;
        }
    }

    public static class NotTamamlaPosition{

        private int position;

        public int getPosition() {
            return position;
        }

        public void setPosition(int position) {
            this.position = position;
        }

        public NotTamamlaPosition(int position) {
            this.position = position;
        }
    }
}
