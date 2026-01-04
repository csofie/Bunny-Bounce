import mediapipe as mp
import math


mediapipe_pose = mp.solutions.pose

# pose function for images
pose_image = mediapipe_pose.Pose(static_image_mode=True, min_detection_confidence=0.5, model_complexity=1)
# pose function for vids
pose_video = mediapipe_pose.Pose(static_image_mode=False, model_complexity=1, min_detection_confidence=0.7,
                                     min_tracking_confidence=0.7);

# for visualizing landmarks
mediapipe_drawing = mp.solutions.drawing_utils


def calculateAngle(landmark1, landmark2, landmark3):

    x1, y1, _ = landmark1
    x2, y2, _ = landmark2
    x3, y3, _ = landmark3

    angle = math.degrees(math.atan2(y3 - y2, x3 - x2) - math.atan2(y1 - y2, x1 - x2))
    if angle < 0:
        angle += 360

    return angle
