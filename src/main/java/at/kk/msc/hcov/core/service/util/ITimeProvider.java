package at.kk.msc.hcov.core.service.util;

import java.time.LocalDateTime;

public interface ITimeProvider {

  /**
   * Returns the current time. Enables mocking.
   *
   * @return a {@link LocalDateTime} object of the current time.
   */
  public LocalDateTime now();
}
