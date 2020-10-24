package email;

public class Main {
    public static void main(String[] args) {

        String mail = new EmailBuilder()
                .subject("Заголовок")
                .to("Начальнику", "Коллеге")
                .to("Еще одному коллеге")
                .copy("Высшему руководству")
                .copy("Другу")
                .content().body("Что же это творится, коллеги?")
                .signature("С уважением").build();

        System.out.println(mail);
    }

}
