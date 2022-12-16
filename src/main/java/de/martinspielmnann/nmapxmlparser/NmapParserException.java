package de.martinspielmnann.nmapxmlparser;

public class NmapParserException extends Exception {

  private static final long serialVersionUID = 1L;

  public NmapParserException() {
    super();
  }

  public NmapParserException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

  public NmapParserException(String message, Throwable cause) {
    super(message, cause);
  }

  public NmapParserException(String message) {
    super(message);
  }

  public NmapParserException(Throwable cause) {
    super(cause);
  }

}
