import Utils
from Utils import calculateAngle, mediapipe_pose


class Movements:

    def __init__(self):
        self.right_shoulder_x = None
        self.left_shoulder_x = None
        self.right_knee_angle = None
        self.left_knee_y = None
        self.left_hip_y = None
        self.right_knee_y = None
        self.right_hip_y = None
        self.left_shoulder_y = None
        self.right_shoulder_y = None
        self.right_wrist_x = None
        self.left_wrist_x = None
        self.left_knee_angle = None
        self.right_hip_angle = None
        self.left_hip_angle = None
        self.right_shoulder_angle = None
        self.left_shoulder_angle = None
        self.right_elbow_angle = None
        self.left_elbow_angle = None
        self.left_leg_distance = None
        self.right_leg_distance = None
        self.wrist_distance = None
        self.left_hip_should_distance = None
        self.right_hip_should_distance = None
        self.previous_right_shoulder_x = None
        self.previous_left_shoulder_x = None
        self.side = None
        self.status = 'Unknown Pose'

    def calculateValues(self, landmarks):
        self.right_knee_angle = calculateAngle(landmarks[mediapipe_pose.PoseLandmark.RIGHT_HIP.value],
                                               landmarks[mediapipe_pose.PoseLandmark.RIGHT_KNEE.value],
                                               landmarks[mediapipe_pose.PoseLandmark.RIGHT_ANKLE.value])

        self.left_knee_y = landmarks[mediapipe_pose.PoseLandmark.LEFT_KNEE.value][1]  # Accessing y-coordinate
        self.left_hip_y = landmarks[mediapipe_pose.PoseLandmark.LEFT_HIP.value][1]
        self.left_shoulder_x, self.left_shoulder_y, _ = landmarks[mediapipe_pose.PoseLandmark.LEFT_SHOULDER.value]

        self.right_knee_y = landmarks[mediapipe_pose.PoseLandmark.RIGHT_KNEE.value][1]
        self.right_hip_y = landmarks[mediapipe_pose.PoseLandmark.RIGHT_HIP.value][1]
        self.right_shoulder_x, self.right_shoulder_y, _ = landmarks[mediapipe_pose.PoseLandmark.RIGHT_SHOULDER.value]

        self.right_wrist_x = landmarks[mediapipe_pose.PoseLandmark.RIGHT_WRIST.value][0]
        self.left_wrist_x = landmarks[mediapipe_pose.PoseLandmark.LEFT_WRIST.value][0]

        self.left_knee_angle = 360 - calculateAngle(landmarks[mediapipe_pose.PoseLandmark.LEFT_HIP.value],
                                                    landmarks[mediapipe_pose.PoseLandmark.LEFT_KNEE.value],
                                                    landmarks[mediapipe_pose.PoseLandmark.LEFT_ANKLE.value])

        self.right_hip_angle = calculateAngle(landmarks[mediapipe_pose.PoseLandmark.LEFT_HIP.value],
                                              landmarks[mediapipe_pose.PoseLandmark.RIGHT_HIP.value],
                                              landmarks[mediapipe_pose.PoseLandmark.RIGHT_KNEE.value])

        self.left_hip_angle = 360 - calculateAngle(landmarks[mediapipe_pose.PoseLandmark.RIGHT_HIP.value],
                                                   landmarks[mediapipe_pose.PoseLandmark.LEFT_HIP.value],
                                                   landmarks[mediapipe_pose.PoseLandmark.LEFT_KNEE.value])

        self.right_shoulder_angle = calculateAngle(landmarks[mediapipe_pose.PoseLandmark.LEFT_SHOULDER.value],
                                                   landmarks[mediapipe_pose.PoseLandmark.RIGHT_SHOULDER.value],
                                                   landmarks[mediapipe_pose.PoseLandmark.RIGHT_ELBOW.value])

        self.left_shoulder_angle = 360 - calculateAngle(landmarks[mediapipe_pose.PoseLandmark.RIGHT_SHOULDER.value],
                                                        landmarks[mediapipe_pose.PoseLandmark.LEFT_SHOULDER.value],
                                                        landmarks[mediapipe_pose.PoseLandmark.LEFT_ELBOW.value])

        self.right_elbow_angle = calculateAngle(landmarks[mediapipe_pose.PoseLandmark.RIGHT_SHOULDER.value],
                                                landmarks[mediapipe_pose.PoseLandmark.RIGHT_ELBOW.value],
                                                landmarks[mediapipe_pose.PoseLandmark.RIGHT_WRIST.value])

        self.left_elbow_angle = 360 - calculateAngle(landmarks[mediapipe_pose.PoseLandmark.LEFT_SHOULDER.value],
                                                     landmarks[mediapipe_pose.PoseLandmark.LEFT_ELBOW.value],
                                                     landmarks[mediapipe_pose.PoseLandmark.LEFT_WRIST.value])

        self.left_leg_distance = abs(self.left_hip_y - self.left_knee_y)
        self.right_leg_distance = abs(self.right_hip_y - self.right_knee_y)
        self.wrist_distance = self.left_wrist_x - self.right_wrist_x
        self.left_hip_should_distance = abs(self.left_hip_y - self.left_shoulder_y)
        self.right_hip_should_distance = abs(self.right_hip_y - self.right_shoulder_y)

    def checkIfIdle(self):
        # 1. if legs are straight - best = 180 degrees
        if ((200 > self.right_knee_angle > 160) and
                (200 > self.left_knee_angle > 160) and
                # 2. if hands are down/to legs - best = 100 degrees (shoulders)
                (120 > self.right_shoulder_angle > 80) and
                (120 > self.left_shoulder_angle > 80) and
                # 3. if legs are together - best = 90 degrees (hips)
                (110 > self.right_hip_angle > 70) and
                (110 > self.left_hip_angle > 70)):
            self.status = 'Idle'
        return self.status

    # open jumping jack position
    def checkIfJumpingJacks(self):
        # if arms are  high enough?
        if ((200 < self.right_shoulder_angle) and
                (200 < self.left_shoulder_angle) and
                # 2. are legs apart?
                (self.right_hip_angle > 95) and
                (self.left_hip_angle > 95)):
            self.status = 'Jumping Jacks'
        return self.status

    def checkIfMarching(self):
        if (self.left_leg_distance < 60) and not (self.right_leg_distance < 60):
            self.status = 'March Left'
        elif (self.right_leg_distance < 60) and not (self.left_leg_distance < 60):
            self.status = 'March Right'
        return self.status

    def checkIfStepTouches(self, width):
        if self.right_shoulder_x <= width//2 and self.left_shoulder_x <= width//2 and self.status != 'Left Step Touch' and self.side != 'left':
            self.side = "left"
            self.status = 'Left Step Touch'
        elif self.right_shoulder_x >= width//2 and self.left_shoulder_x >= width//2 and self.status != 'Right Step Touch' and self.side != 'right':
            self.side = "right"
            self.status = 'Right Step Touch'
        elif self.right_shoulder_x <= width//2 and self.left_shoulder_x >= width//2 and self.status != 'Center Step Touch' and self.side != 'center':
            self.side = "center"
            self.status = 'Center Step Touch'
        return self.status

    def checkIfBurpees(self):
        # Burpees
        # First - Burpee Down
        # 1. are shoulders at ~ 90 degree angles?
        if ((110 > self.right_shoulder_angle > 70) and
             (110 > self.left_shoulder_angle > 70) and

             # 2. if hips and shoulders are close enough
             (self.right_hip_should_distance < 70) and
             (self.left_hip_should_distance < 70)):
            self.status = 'Burpee Down'

        # Second - Burpee Up
        # 1. are legs together?
        # 2. are arms up?
        elif ((95 > self.right_hip_angle) and
                (95 > self.left_hip_angle) and
                (self.right_shoulder_angle > 230) and
                (self.left_shoulder_angle > 230)):
            self.status = 'Burpee Up'

        return self.status
