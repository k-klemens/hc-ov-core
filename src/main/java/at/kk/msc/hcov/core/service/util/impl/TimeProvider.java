package at.kk.msc.hcov.core.service.util.impl;

import at.kk.msc.hcov.core.service.util.ITimeProvider;
import java.time.LocalDateTime;
import org.springframework.stereotype.Service;

@Service
public class TimeProvider implements ITimeProvider {

  public LocalDateTime now() {
    return LocalDateTime.now();
  }

}
