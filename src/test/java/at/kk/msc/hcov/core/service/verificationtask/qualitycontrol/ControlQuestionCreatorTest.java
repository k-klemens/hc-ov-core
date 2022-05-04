package at.kk.msc.hcov.core.service.verificationtask.qualitycontrol;

import static at.kk.msc.hcov.core.util.mockdata.QualityControlTaskMockData.EXPECTED_QC_TASKS_AS_VERIFICATION_WITHOUT_CONTEXT;
import static at.kk.msc.hcov.core.util.mockdata.QualityControlTasksSpecificationMockData.MOCKED_QUALITY_CONTROL_TASK_SPECIFICATION;
import static at.kk.msc.hcov.core.util.mockdata.VerificationTaskSpecificationMockData.MOCKED_VERIFICATION_TASK_SPECIFICATION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import at.kk.msc.hcov.core.service.exception.PluginLoadingError;
import at.kk.msc.hcov.core.service.templating.model.ResolvedVariablesWrapper;
import at.kk.msc.hcov.core.service.verificationtask.qualitycontrol.exception.QualityControlTaskCreationFailedException;
import at.kk.msc.hcov.core.service.verificationtask.qualitycontrol.impl.ControlQuestionCreator;
import at.kk.msc.hcov.core.service.verificationtask.qualitycontrol.model.QualityControlTask;
import at.kk.msc.hcov.core.service.verificationtask.qualitycontrol.model.QualityControlTasksSpecification;
import at.kk.msc.hcov.core.service.verificationtask.task.IVerificationTaskCreator;
import at.kk.msc.hcov.core.service.verificationtask.task.exception.VerificationTaskCreationFailedException;
import at.kk.msc.hcov.core.service.verificationtask.task.model.VerificationTaskSpecification;
import at.kk.msc.hcov.core.util.mockdata.QualityControlTaskMockData;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ControlQuestionCreatorTest {

  @Mock
  IVerificationTaskCreator verificationTaskCreatorMock;
  IQualityControlProvider target;

  @BeforeEach
  void setUp() {
    target = new ControlQuestionCreator(verificationTaskCreatorMock);
  }

  @Test
  void testCreateQualityControlTasks()
      throws VerificationTaskCreationFailedException, PluginLoadingError, QualityControlTaskCreationFailedException {
    // given
    QualityControlTasksSpecification givenQualityControlSpecification = MOCKED_QUALITY_CONTROL_TASK_SPECIFICATION();
    VerificationTaskSpecification givenVerificationSpecification = MOCKED_VERIFICATION_TASK_SPECIFICATION();
    givenVerificationSpecification.setQualityControlTasksSpecification(givenQualityControlSpecification);

    List<ResolvedVariablesWrapper> givenVariablesWrapper = givenQualityControlSpecification.getResolvedVariablesWrappers();


    when(verificationTaskCreatorMock.createTasksWithResolvedVariables(
        eq(givenVerificationSpecification), eq(givenVariablesWrapper)
    )).thenReturn(EXPECTED_QC_TASKS_AS_VERIFICATION_WITHOUT_CONTEXT());

    // when
    List<QualityControlTask> actual = target.createQualityControlTasks(givenVerificationSpecification);

    // then
    assertThat(actual).containsExactlyInAnyOrderElementsOf(QualityControlTaskMockData.EXPECTED_QC_TASKS_WITHOUT_CONTEXT());
  }

  @Test
  void testCreateQualityControlTasks_givenVerificationTaskCreatorThrowsException_expectQualityControlException()
      throws VerificationTaskCreationFailedException, PluginLoadingError, QualityControlTaskCreationFailedException {
    // given
    QualityControlTasksSpecification givenQualityControlSpecification = MOCKED_QUALITY_CONTROL_TASK_SPECIFICATION();
    VerificationTaskSpecification givenVerificationSpecification = MOCKED_VERIFICATION_TASK_SPECIFICATION();
    givenVerificationSpecification.setQualityControlTasksSpecification(givenQualityControlSpecification);

    List<ResolvedVariablesWrapper> givenVariablesWrapper = givenQualityControlSpecification.getResolvedVariablesWrappers();


    when(verificationTaskCreatorMock.createTasksWithResolvedVariables(
        eq(givenVerificationSpecification), eq(givenVariablesWrapper)
    )).thenThrow(new VerificationTaskCreationFailedException(new Exception("MOCK-Exception")));

    // when - then
    assertThatThrownBy(() -> target.createQualityControlTasks(givenVerificationSpecification))
        .isInstanceOf(QualityControlTaskCreationFailedException.class);
  }

}
