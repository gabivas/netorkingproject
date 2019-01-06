package utils;

public interface Constants {

    int SERVER_PORT = 8080;
    String START_SERVER_MESSAGE = "Server is running, type 'exit' to stop it.";

    int LOG_IN = 1;
    int LOG_OUT = 0;
    int TRY = 2;

    int USER_EXISTS = 3;
    String USER_EXISTS_MESSAGE = "Utilizatorul cu acest nume exista deja";

    int SUCCESSFULLY_LOG_IN = 4;
    String SUCCESSFULLY_LOG_IN_MESSAGE = "Te-ai logat cu succes. Mult noroc in alergera numarului, ";

    int SUCCESSFULLY_LOG_OUT = 5;
    String SUCCESSFULLY_LOG_OUT_MESSAGE = "Te-ai delogat cu succes, ";

    int NUMBER_FOUND = 6;
    String NUMBER_FOUND_MESSAGE = "Numarul a fost descoperit de catre ";

    int INVALID_NUMBER = 7;
    String INVALID_NUMBER_MESSAGE = "Numarul trebuie sa aiba 4 cifre";

    int TRY_RESPONSE = 8;
}
