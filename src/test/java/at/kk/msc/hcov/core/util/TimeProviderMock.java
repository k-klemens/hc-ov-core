package at.kk.msc.hcov.core.util;

import at.kk.msc.hcov.core.service.util.ITimeProvider;
import java.time.LocalDateTime;
import org.springframework.boot.test.context.TestComponent;

@TestComponent
public class TimeProviderMock implements ITimeProvider {

  public static final LocalDateTime MOCK_TIME = LocalDateTime.of(2022, 05, 03, 17, 30);


  @Override
  public LocalDateTime now() {
    return MOCK_TIME;
  }
}
