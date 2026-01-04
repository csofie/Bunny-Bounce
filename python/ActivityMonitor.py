import time


class ActivityMonitor:
    def __init__(self):
        self.current_status = 'Unknown Pose'
        self.LeewayTime = 1.0  # seconds that you are allowed to have between poses

        # burpees get special leeway because it takes me longer than a full sec to do a burpee lol
        self.BurpeeLeewayTime = 1.6

        self.last_time = time.time()
        self.last_jumping_jack_time = None  # Keep track of the last valid jumping jack
        self.last_march_time = None
        self.last_step_touch_time = None
        self.last_burpee_time = None

        self.doing_jumping_jacks = False
        self.doing_idle = False
        self.doing_marches = False
        self.doing_step_touches = False
        self.doing_burpees = False

        self.last_march_pos = None
        self.last_jumping_jack_pos = None
        self.last_burpee_pos = None
        self.last_step_touch_pos = None

        self.last_step_zone = "center"  # character in game starts center
        # If user starts on left or right side of screen, a slide should occur immediately

        # If you hold jumping jack open pose, it switches to unknown pose (if it goes over leeway time).
        # However, the frame after will switch back to the status "Jumping Jacks" since it was unknown pose previously.
        # Idle lock is used to prevent jumping jack open pose from being held.
        self.jj_locked_until_idle = False
        self.marches_locked_until_idle = False  # Same logic applies.
        self.burpees_locked_until_idle = False

    def update_status(self, new_status):
        current_time = time.time()

        # If user returns to idle after holding open jumping jack pose, then jumping jacks will unlock.
        if new_status == "Idle" and self.jj_locked_until_idle:
            self.jj_locked_until_idle = False
        if new_status == "Idle" and self.marches_locked_until_idle:
            self.marches_locked_until_idle = False
        if new_status == "Idle" and self.burpees_locked_until_idle:
            self.burpees_locked_until_idle = False

        # Jumping Jacks ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        if new_status == 'Jumping Jacks':

            if self.jj_locked_until_idle:
                self.current_status = 'Unknown Pose'
                return

            # make sure we aren't just holding the open pose; continuous movement is necessary
            if self.doing_jumping_jacks and self.last_jumping_jack_pos == 'open':
                if (current_time - self.last_jumping_jack_time) >= self.LeewayTime:
                    self.doing_jumping_jacks = False
                    self.last_jumping_jack_pos = None
                    self.current_status = 'Unknown Pose'
                    self.jj_locked_until_idle = True
                    return

            if not self.doing_jumping_jacks:
                # print("Jumping Jacks started!")
                self.doing_jumping_jacks = True
                self.last_jumping_jack_pos = 'open'
                self.last_jumping_jack_time = current_time
            elif self.last_jumping_jack_pos == 'closed':
                self.last_jumping_jack_pos = 'open'
                self.last_jumping_jack_time = current_time

            self.doing_marches = False
            self.doing_step_touches = False
            self.doing_burpees = False
            self.current_status = new_status

        elif self.doing_jumping_jacks and (new_status == 'Idle' or new_status == 'Unknown Pose'):
            if (current_time - self.last_jumping_jack_time) < self.LeewayTime:
                if self.last_jumping_jack_pos == 'open' and new_status == "Idle":
                    self.last_jumping_jack_pos = 'closed'
                    self.last_jumping_jack_time = current_time
                    print("Jumping Jack performed!")

                self.current_status = 'Jumping Jacks'  # Maintain status if under LeewayTime seconds
            else:
                self.doing_jumping_jacks = False  # Stop jumping jacks if over LeewayTime seconds
                self.last_jumping_jack_pos = None
                self.current_status = new_status
                # print("Jumping Jacks stopped.")

        # Marching ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        elif new_status == 'March Left' or new_status == 'March Right':

            if self.marches_locked_until_idle:
                self.current_status = 'Unknown Pose'
                return

            # ensuring continuous movement
            if self.doing_marches and self.last_march_pos == new_status:
                if (current_time - self.last_march_time) >= self.LeewayTime:
                    self.doing_marches = False
                    self.last_march_pos = None
                    self.current_status = 'Unknown Pose'
                    self.marches_locked_until_idle = True
                    return

            if not self.doing_marches:
                print("Marches started!")
                self.doing_marches = True
                self.last_march_pos = new_status
                self.last_march_time = current_time
            elif self.last_march_pos != new_status:  # e.g. it was march left and now its march right
                self.last_march_pos = new_status
                self.last_march_time = current_time

            self.doing_jumping_jacks = False
            self.doing_step_touches = False
            self.doing_burpees = False
            self.current_status = new_status

        elif self.doing_marches and (new_status == 'Idle' or new_status == 'Unknown Pose'):
            if (current_time - self.last_march_time) < self.LeewayTime:
                self.current_status = self.last_march_pos  # Maintain status if under LeewayTime seconds
            else:
                self.doing_marches = False  # Stop status if over LeewayTime seconds
                self.last_march_pos = None
                self.current_status = new_status
                print("Marches stopped.")

        # Stepping ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        elif new_status in ('Right Step Touch', 'Left Step Touch', 'Center Step Touch'):

            self.last_step_touch_time = current_time
            self.last_step_touch_pos = new_status

            if not self.doing_step_touches:
                print(new_status, "performed!")
            self.doing_step_touches = True

            self.doing_jumping_jacks = False
            self.doing_marches = False
            self.doing_burpees = False
            self.current_status = new_status

        elif self.doing_step_touches and (new_status == 'Idle' or new_status == 'Unknown Pose'):
            if (current_time - self.last_step_touch_time) < self.LeewayTime:
                self.current_status = self.last_step_touch_pos  # Maintain status if under LeewayTime seconds
            else:
                self.doing_step_touches = False  # Stop status if over self.LeewayTime seconds
                self.current_status = new_status
                # print("Step Touch stopped.")

        # Burpees ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        elif new_status == 'Burpee Up' or new_status == 'Burpee Down':

            if self.burpees_locked_until_idle:
                self.current_status = 'Unknown Pose'
                return

            # ensuring continuous movement
            if self.doing_burpees and self.last_burpee_pos == new_status:
                if (current_time - self.last_burpee_time) >= self.LeewayTime:
                    self.doing_burpees = False
                    self.last_burpee_pos = None
                    self.current_status = 'Unknown Pose'
                    self.burpees_locked_until_idle = True
                    return

            if not self.doing_burpees:
                # print("Burpees started!")
                self.doing_burpees = True
                self.last_burpee_pos = new_status
                self.last_burpee_time = current_time
            elif self.last_burpee_pos != new_status:  # e.g. its burpee down and before it was burpee up
                self.last_burpee_pos = new_status
                self.last_burpee_time = current_time
                print("Burpee performed!")


            self.doing_jumping_jacks = False
            self.doing_marches = False
            self.doing_step_touches = False
            self.current_status = new_status

        elif self.doing_burpees and (new_status == 'Idle' or new_status == 'Unknown Pose'):
            if (current_time - self.last_burpee_time) < self.LeewayTime:
                self.current_status = self.last_burpee_pos  # Maintain status if under self.LeewayTime seconds
            else:
                self.doing_burpees = False  # Stop status if over self.LeewayTime seconds
                self.last_burpee_pos = None
                self.current_status = new_status
                # print("Burpees stopped.")

        # ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
        else:
            self.current_status = new_status
            self.last_time = current_time

    def get_status(self):
        return self.current_status
