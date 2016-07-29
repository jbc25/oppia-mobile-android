import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.digitalcampus.oppia.listener.DeleteCourseListener;
import org.digitalcampus.oppia.listener.InstallCourseListener;
import org.digitalcampus.oppia.model.Course;
import org.digitalcampus.oppia.model.DownloadProgress;
import org.digitalcampus.oppia.task.DeleteCourseTask;
import org.digitalcampus.oppia.task.InstallDownloadedCoursesTask;
import org.digitalcampus.oppia.task.Payload;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

import Utils.CourseUtils;
import Utils.FileUtils;

@RunWith(AndroidJUnit4.class)
public class DeleteCourseTest {

    private final String CORRECT_COURSE = "Wash.zip";

    private Context context;
    private CountDownLatch signal;
    private Payload response;

    @Before
    public void setUp() throws Exception {
        context = InstrumentationRegistry.getTargetContext();
        signal = new CountDownLatch(1);
    }

    @After
    public void tearDown() throws Exception {
        signal.countDown();
    }

    @Test
    public void deleteCourse_success() throws Exception{
        String filename = CORRECT_COURSE;

        FileUtils.copyZipFromAssets(filename);  //Copy course zip from assets to download path

        ArrayList<Object> data = new ArrayList<>();
        Payload payload = new Payload(data);
        InstallDownloadedCoursesTask imTask = new InstallDownloadedCoursesTask(context);
        imTask.setInstallerListener(new InstallCourseListener() {
            @Override
            public void downloadComplete(Payload p) {  }

            @Override
            public void downloadProgressUpdate(DownloadProgress dp) {  }

            @Override
            public void installComplete(Payload r) {
                response = r;
                signal.countDown();
            }

            @Override
            public void installProgressUpdate(DownloadProgress dp) {  }
        });
        imTask.execute(payload);

        signal.await();

        Course c = (Course)response.getData().get(0);

        DeleteCourseTask task = new DeleteCourseTask(context);
        ArrayList<Object> payloadData = new ArrayList<>();
        payloadData.add(c);
        Payload p = new Payload(payloadData);
        task.setOnDeleteCourseListener(new DeleteCourseListener() {
            @Override
            public void onCourseDeletionComplete(Payload r) {
                response = r;
                signal.countDown();
            }
        });
        task.execute(p);

        signal.await();

    }

}
