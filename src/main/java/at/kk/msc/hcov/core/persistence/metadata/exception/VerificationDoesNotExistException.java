package at.kk.msc.hcov.core.persistence.metadata.exception;

public class VerificationDoesNotExistException extends Exception {

  public VerificationDoesNotExistException(String verificationName) {
    super("Verification '" + verificationName + "' does not exist!");
  }
}
