import java.time.ZonedDateTime;

public class T2 {
    public static void main(String[] args) {
        ZonedDateTime zbj = ZonedDateTime.now();//默认时区
        System.out.println(zbj);
        //2022-03-14T17:26:56.529+08:00[Asia/Shanghai]
    }
}
