package at.kk.msc.hcov.core.service.processing.exception;

public class DataProcessorException extends Exception {

  public DataProcessorException(String message) {
    super(message);
  }

  public DataProcessorException() {
  }

  public DataProcessorException(String message, Throwable cause) {
    super(message, cause);
  }
}
