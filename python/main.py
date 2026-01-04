import math
import cv2
import matplotlib.pyplot as plt

import Utils
from ActivityMonitor import ActivityMonitor
from Movements import Movements
from math import hypot
from Utils import mediapipe_pose, mediapipe_drawing, pose_image, pose_video
import sys

monitor = ActivityMonitor()
movements = Movements()
#usedMovements = sys.argv[1].split(",")
usedMovements = {"Jumping Jacks", "Burpees", "Step Touches", "Marches"}
print(usedMovements)
sys.stdout.flush()

# performs pose detection for most prominent person in an img
def detectPose(image, pose, draw=False, display=False):

    output_image = image.copy()

    # whyy bgr
    imageRGB = cv2.cvtColor(image, cv2.COLOR_BGR2RGB)
    results = pose.process(imageRGB) # pose detection

    height, width, _ = image.shape
    landmarks = []

    if results.pose_landmarks:  #  if landmarks detected

        for landmark in results.pose_landmarks.landmark:
            landmarks.append((int(landmark.x * width), int(landmark.y * height), int(landmark.z * width)))

        if draw:  # draws w/ connected lines
            mediapipe_drawing.draw_landmarks(image=output_image, landmark_list=results.pose_landmarks,
                                             connections=mediapipe_pose.POSE_CONNECTIONS,
                                             landmark_drawing_spec=mediapipe_drawing.DrawingSpec(color=(255, 255, 255),
                                                                                                 thickness=3,
                                                                                                 circle_radius=3),
                                             connection_drawing_spec=mediapipe_drawing.DrawingSpec(color=(49, 125, 237),
                                                                                                   thickness=2,
                                                                                                   circle_radius=2))

    if display:
        plt.figure(figsize=[15, 15])  # in inches
        plt.subplot(121);
        plt.imshow(image[:, :, ::-1]);         # bgr -> rgb
        plt.title("Original Image");
        plt.axis('off');
        plt.subplot(122);
        plt.imshow(output_image[:, :, ::-1]);
        plt.title("Output Image");
        plt.axis('off');
        plt.show()


    else:
        return output_image, landmarks


#  testing w/ checking if hands are joined
'''
def checkHandsJoined(image, landmarks, draw=False, display=False):
    height, width, _ = image.shape
    output_image = image.copy()
    left_wrist_landmark = (landmarks[mediapipe_pose.PoseLandmark.LEFT_WRIST].x * width,
                           landmarks[mediapipe_pose.PoseLandmark.LEFT_WRIST].y * height)

    right_wrist_landmark = (landmarks[mediapipe_pose.PoseLandmark.RIGHT_WRIST].x * width,
                            landmarks[mediapipe_pose.PoseLandmark.RIGHT_WRIST].y * height)
    
    euclidean_distance = int(hypot(left_wrist_landmark[0] - right_wrist_landmark[0],
                                   left_wrist_landmark[1] - right_wrist_landmark[1]))

    if euclidean_distance < 70:
        hand_status = 'Hands Joined'
        color = (0, 255, 0)
    else:
        hand_status = 'Hands Not Joined'
        color = (0, 0, 255)

    if draw:
        cv2.putText(output_image, hand_status, (10, 30), cv2.FONT_HERSHEY_PLAIN, 2, color, 3)
        cv2.putText(output_image, f'Distance: {euclidean_distance}', (10, 70),
                    cv2.FONT_HERSHEY_PLAIN, 2, color, 3)

    if display:
        plt.figure(figsize=[10, 10])
        plt.imshow(output_image[:, :, ::-1]);
        plt.title("Output Image");
        plt.axis('off')
        plt.show()

    else:
        return output_image, hand_status
'''


def checkIfIdle(image, landmarks, draw=False, display=False):
    height, width, _ = image.shape
    output_image = image.copy()

    # its left from the IMAGE perspective, right in R.L.
    movements.calculateValues(landmarks)

    movements.status = 'Unknown Pose'

    status = movements.checkIfIdle()
    if "Jumping Jacks" in usedMovements:
        status = movements.checkIfJumpingJacks()
    if "Marches" in usedMovements:
        status = movements.checkIfMarching()
    if "Step Touches" in usedMovements:
        status = movements.checkIfStepTouches(width)
    if "Burpees" in usedMovements:
        status = movements.checkIfBurpees()

    sys.stdout.flush()

    monitor.update_status(status)
    status = monitor.get_status()

    display_status = status
    if status == 'March Left' or status == 'March Right':
        display_status = 'Marches'
    elif status == 'Burpee Up' or status == 'Burpee Down':
        display_status = 'Burpees'

    if status == 'Unknown Pose':
        color = (0, 0, 255)  # BGR - red
    else:
        color = (123, 221, 97)  # BGR - green

    if draw:
        # Classified Hand status
        cv2.putText(output_image, "Pose Status", (10, 30), cv2.FONT_HERSHEY_PLAIN, 2, color, 3)

        cv2.putText(output_image, f'Status: {display_status}', (10, 70),
                    cv2.FONT_HERSHEY_PLAIN, 2, color, 3)

        # Angle tests
        cv2.putText(output_image, f'Left hip: {movements.right_hip_angle}', (10, 110),
                    cv2.FONT_HERSHEY_PLAIN, 2, color, 3)
        cv2.putText(output_image, f'Right hip: {movements.left_hip_angle}', (10, 150),
                    cv2.FONT_HERSHEY_PLAIN, 2, color, 3)
        cv2.putText(output_image, f'Left Shoulder: {movements.right_shoulder_angle}', (10, 190),
                    cv2.FONT_HERSHEY_PLAIN, 2, color, 3)
        cv2.putText(output_image, f'Right Shoulder: {movements.left_shoulder_angle}', (10, 230),
                    cv2.FONT_HERSHEY_PLAIN, 2, color, 3)
        cv2.putText(output_image, f'Left Knee: {movements.right_knee_angle}', (10, 270),
                    cv2.FONT_HERSHEY_PLAIN, 2, color, 3)
        cv2.putText(output_image, f'Right Dist: {movements.left_leg_distance}', (10, 350),
                    cv2.FONT_HERSHEY_PLAIN, 2, color, 3)
        cv2.putText(output_image, f'Left Dist: {movements.right_leg_distance}', (10, 310),
                    cv2.FONT_HERSHEY_PLAIN, 2, color, 3)
        cv2.putText(output_image, f'Wrist Dist: {movements.wrist_distance}', (10, 390),
                    cv2.FONT_HERSHEY_PLAIN, 2, color, 3)
        cv2.putText(output_image, f'Right Elbow: {movements.left_elbow_angle}', (10, 430),
                    cv2.FONT_HERSHEY_PLAIN, 2, color, 3)
        cv2.putText(output_image, f'Left Elbow: {movements.right_elbow_angle}', (10, 470),
                    cv2.FONT_HERSHEY_PLAIN, 2, color, 3)
        cv2.putText(output_image, f'right dist 2: {movements.left_hip_should_distance}', (10, 510),
                    cv2.FONT_HERSHEY_PLAIN, 2, color, 3)
        cv2.putText(output_image, f'left dist 2: {movements.right_hip_should_distance}', (10, 550),
                    cv2.FONT_HERSHEY_PLAIN, 2, color, 3)

    if display:
        plt.figure(figsize=[10, 10])  # inches
        plt.imshow(output_image[:, :, ::-1])
        plt.title("Output Image")
        plt.axis('off')
        plt.show()

    else:
        return output_image, status


# VideoCapture object that reads from the webcam
# 1 is external webcam (if connected) (for me since I have no internal I will use 0 for my external webcam)
camera_video = cv2.VideoCapture(0)
camera_video.set(3, 1280)  # camera frame width (which has an index of 3) to 1280px
camera_video.set(4, 960)  # camera frame height (which has an index of 4) to 960px

# cv2.namedWindow('Olympus Ascension', cv2.WINDOW_NORMAL)

while camera_video.isOpened():
    ok, frame = camera_video.read()
    if not ok:
        print("Not ok :(")
        break

    frame = cv2.flip(frame, 1)  # flip frame horizontally for "selfie-view"
    frame_height, frame_width, _ = frame.shape

    # pose detection
    frame, landmarks = detectPose(frame, pose_video, draw=True)

    if landmarks:
        frame, status = checkIfIdle(frame, landmarks, draw=True)

    # 
    # # display the frame
    # cv2.imshow('Olympus Ascension', frame)
    #
    # k = cv2.waitKey(1) & 0xFF
    #
    # #  if 'esc' is pressed, break the loop
    # if (k == 27):
    #     break

#camera_video.release()
cv2.destroyAllWindows()

'''
# test
IMG_PATH = 'media/sample image.png'
image = cv2.imread(IMG_PATH)
detectPose(image, pose_image, draw=True, display=True)
'''

'''
IMG_PATH = 'media/Step Touch Right.jpg'
image = cv2.imread(IMG_PATH)
image, landmarks = detectPose(image, pose_image, draw=True)
if landmarks:
    checkIfIdle(image, landmarks, draw=True, display=True)
'''