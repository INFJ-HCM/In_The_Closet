//출처 : https://blog.naver.com/PostView.nhn?blogId=snscho66&logNo=100131461219&categoryNo=0&parentCategoryNo=0&viewDate=&currentPage=3&postListTopCurrentPage=&from=postList&userTopListOpen=true&userTopListCount=5&userTopListManageOpen=false&userTopListCurrentPage=3

private void InitializeNui()
{
    try
    {
        //_kinectNui를 Runtime 오브젝트로 선언, 이것은 키넥트 센서 인스턴스이다.
        _kinectNui = new Runtime();

        //비디오와 뎁스 스트림을 오픈하고, 이벤트 핸드러를 초기화한다.
        //다른 키넥트 함수를 부르기 전에 반드시 초기화를 해준다.
        _kinectNui.Initailize(RuntimeOptions.UseDepthAndPlayerIndex |
                                RuntimeOptions.UseSkeletalTracking | RuntimeOptions.UseColor);

        //스트림 칼라 이미지:
        //*UseColor옵션을 반드시 포함한다.
        //*인텍스의 유효한 해상도는 320x240와 80x60이다.
        //*유효한 이미지 타입은 DepthAndPlayerIndex뿐이다.
        _kinectNui.DepthStream.Open(ImageStreamType.Depth, 2, ageResolution.Resolution320x240, ImageType.DepthAndPlayerIndex);
        lastTime = DateTime.Now;

        _kinectNui.VideoFrameReady += new EventHandler<ImageFrameReadyEventArgs>
            NuiVideoFrameReady);
        _kinectNui.DepthFrameReady += new EventHandler<ImageFrameReadyEventArgs>
            (nui_DepthFrameReady);
    }
    catch (InvalidOperationException ex)
    {
        MessageBox.Show(ex.Message);
    }
}

//3단계 : 비디오 출력

void NuiVideoFrameReady(object sender, ImageFrameReadyEventArgs e)
{
    PlanarImage Image = e.ImageFrame.Image;

    image.Source = BitmapSource.Create(
        Image.Width, Image.Height, 96, 96, PixelFormats.Bgr32, null, 
        Image.Bits, Image.Width * Image.BytesPerPixel);

    imageCmyk32.Source = BitmapSource.Create(
        Image.Width, Image.Height, 96, 96, PixelFormats.Cmyk32, null,
        Image.Bits, Image.Width * Image.BytesPerPixel);
}
// 뎁스 프레임 레디 이벤트 핸들러
void nui_DepthFrameReady(object sender, ImageFrameReadyEventArgs e)
{
    var Image = e.ImageFrame.Image;
    var convertedDepthFrame = convertedDepthFrame(Image.Bits);

    depth.Source = BitmapSource.Create(
        Image.Width, Image.Height, 96, 96, PixelFormats.Bgr32, null, convertedDepthFrame, 
        Image.Width * 4);

    CalculateFps();
}

//플레이어 인덱스를 포함한 16비트 그레이스케일 뎁스 프레임을 32비트 프레임으로 바꾼다.
//다른 사용자는 다른 컬러로 표현한다.
byte[] convertDepthFrame(byte[] depthFrame16)
{
    for (int i16 = 0, i32 = 0; i16 < depthFrame16.Length && i32 < depthFrame32.Length; i16 += 2, i32 += 4)
    {
        int player = depthFrame16[i16] & 0x07;
        int realDepth = (depthFrame16[i16 + 1] << 5) | (depthFrame16[i16] >> 3);
        //13비트 뎁스 정보를 8비트로 바꾼다.
        //이것은 화면 표시를 위한 것이다.
        byte intensity = (byte)(255 - (255 * realDepth / 0x0fff));

        depthFrame32[i32 + RED_IDX] = intensity;
        depthFrame32[i32 + BLUE_IDX] = intensity;
        depthFrame32[i32 + GREEN_IDX] = intensity;
    }
    return depthFrame32;
}

void CalculateFps()
{
    ++totalFrames;

    var cur = DateTime.Now;
    if (cur.Subtract(lastTime) > TimeSpan.FromSeconds(1))
    {
        int frameDiff = totalFrames - lastFrames;
        lastFrames = totalFrames;
        lastTime = cur;
        frameRate.Text = frameDiff.ToString() + "fps";
    }
}

//4단계 : 카메라 각도 조절
//키넥트를 초기화한 다음 카메라 오브젝트를 만든다.

private Camera _cam;
_cam = _kinectNui.NuiCamera;
txtCameraName.Text = _cam.UniqueDeviceName;


//카메라 클래스의 정의
namespace Microsoft.Research.Kinect.Nui
{
    public class Camera
    {
        public static readonly int ElevationMaximum;
        public static readonly int ElevationMinimum;

        public void GetColorPixelCoordinatesFronDepthPixel(ImageResolution colorResolution, ImageViewArea viewArea, int depthX, int depthY, short depthValue, out int colorX, out int colorY);
    }
}

//5단계 : 업 다운
private void BtnCameraUpClick(object sender, RoutedEventArgs e)
{
    try
    {
        _cam.ElevationAngle = _cam.ElevationAngle + 5;
    }
    catch (InvalidOperationException ex)
    {
        MessageBox.Show(ex.Message);
    }
    catch (ArgumentOutRangeException outRangeException)
    {
        //Elevation angle must be between Elevation Minimum/Maximum"
        MessageBox.Show(outRangeException.Message);
    }
}

//다음은 다운이다.
private void BtnCameraDownClick(object sender, RoutedEventArgs e)
{
    try
    {
        _cam.ElevationAngle = _cam.ElevationAngle - 5;
    }
    catch (InvalidOperationException ex)
    {
        MessageBox.Show(ex.Message);
    }
    catch (ArgumentOutOfRangeException outOfRangeException)
    {
        //Elevation angle must be between Elevation Minimum/Maximum"
        MessageBox.Show(outOfRangeException.Message);
    }
}

//6단계 : SkeletonFrameReady에 등록
//UseSkeletalTracking 옵션으로 초기화한다. 이 옵션을 안 주면 뼈대 추적이 안된다.
_kinectNui.Initialize(RuntimeOptions.UseColor | RuntimeOptions.UseSkeletalTracking | RuntimeOptions.UseColor);
_kinectNui.SkeletonFrameReady += new EventHandler<SkeletonFrameReadyEventArgs>(SkeletonFrameReady);

//뼈대 추적 위치값은 다음의 enum값이다.
namespace Microsoft.Research.Kinect.Nui
{
    public enum JointD
    {
        HipCenter,
        Spine,
        ShoulderCenter,
        Head,
        ShoulderLeft,
        ElbowLeft,
        WristLeft,
        HandLeft,
        ShoulderRight,
        ElbowRight,
        WristRight,
        HandRight,
        HipLeft,
        KneeLeft,
        AnkleLeft,
        FootLeft,
        HipRight,
        KneeRight,
        AnkleRight,
        FootRight,
        Count,
    }
}

//7단계 : 뼈대 위치(Joint Position) 얻기
private Point getDisplayPosition(Joint joint)
{
    float depthX, depthY;
    _kinectNui.SkeletonEngine.SkeletonToDepthImage(joint, Position, out depthX, out depthY);
    depthX = Math.Max(0, Math.Min(depthX * 320, 320)); //convert to 320, 240 space
    depthY = Math.Max(0, Math.Min(depthY * 240, 240)); //convert to 320, 240 space
    int colorX, colorY;
    ImageViewArea iv = new ImageViewArea();
    //only ImageResolution.Resolution640x480 is supported at this point
    _kinectNui.NuiCamera.GetColorPixelVoordinatesFromDepthPixel(ImageResolution.Resolution640x480, iv, (int)depthX, (int)depthY, (short)0, out colorX, out colorY);
    //map back to skeleton.Width & skeleton.Height return new Point(int) (imageContainer.Width * colorX / 640.0) - 30, (int)(imageContainer.Height * colorY / 480) - 30);
}

//8단계 : 뼈대 타입에 따라 이미지 삽입
//Vector4(x, y, z, w)를 사용하여 뼈대의 중심점을 표현한다. 여기서 x, y, z는 카메라 공간 좌표이고, w는 품질레벨을 말한다.(0-1 사이의 값이다.)
void SkeletonFrameReady(object sender, SkeletonFrameReadyEventArgs e)
{
    foreach (SkeletonData data in e.SkeletonFrame.Skeletons)
    {
        //트랙킹이 되는 뼈대인지 아닌지 판별한다. 트랙킹이 안되는 것이면 위치값만 얻을 수 있다.
        if (SkeletonTrackingState.Tracked != data.TrackingState) continue;

        //각 관절의 위치값은 Vector4 : (x, y, z, w)로 표현된다.
        foreach (Joint joint in data.Joints)
        {
            if (joint.Position.W < 0.6f) return; //Quality check
            switch (joint.ID)
            {
                case JointID.Head :
                    var heanp -getDisplayPosition(joint);

                    Canvas.SetLeft(imgHead, heanp.X);
                    Canvas.SetTop(imgHead, heanp.Y);
                    break;

                case JointID.HandRight :
                    var rhp = getDisplayPosition(joint);

                    Canvas.SetLeft(imgRightHand, rhp.X);
                    Canvas.SetTop(imgRightHand, rhp.Y);
                    break;

                case JointID.HandLeft :
                    var lhp = getDisplayPosition(joint);

                    Canvas.SetLeft(imgRightHand, rhp.X);
                    Canvas.SetTop(imgRightHand, rhp.Y);
                    break;
            }
        }
    }
}
