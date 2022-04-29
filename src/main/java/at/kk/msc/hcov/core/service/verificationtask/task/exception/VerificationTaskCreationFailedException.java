package at.kk.msc.hcov.core.service.verificationtask.task.exception;

public class VerificationTaskCreationFailedException extends Exception {

  public VerificationTaskCreationFailedException(Exception e) {
    super("Failed to create verification task due to:" + e.getMessage(), e);
  }

}
