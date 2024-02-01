package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp
public class ControlsNEW extends LinearOpMode {
    private Servo ClawHand;
    private Servo Airplane;
    private DcMotor ClawIntLeft;
    private DcMotor ClawIntRight;
    private Servo ClawPos;
    private Servo IntakeStopper;

    @Override
    public void runOpMode() throws InterruptedException {
        // Declare our motors
        // Make sure your ID's match your configuration
        DcMotor frontLeftMotor = hardwareMap.dcMotor.get("leftFront");
        DcMotor backLeftMotor = hardwareMap.dcMotor.get("leftBack");
        DcMotor frontRightMotor = hardwareMap.dcMotor.get("rightFront");
        DcMotor backRightMotor = hardwareMap.dcMotor.get("rightBack");
        ClawHand = hardwareMap.get(Servo.class, "ClawHand");
        Airplane = hardwareMap.get(Servo.class, "Airplane");
        ClawIntLeft = hardwareMap.get(DcMotor.class, "ClawIntLeft");
        ClawIntRight = hardwareMap.get(DcMotor.class, "ClawIntRight");
        ClawPos = hardwareMap.get(Servo.class, "ClawPos");
        IntakeStopper = hardwareMap.get(Servo.class, "IntakeStopper");


        // Reverse the right side motors. This may be wrong for your setup.
        // If your robot moves backwards when commanded to go forwards,
        // reverse the left side instead.
        // See the note about this earlier on this page.
        frontRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        backRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        ClawHand.setPosition(1);
        ClawPos.setPosition(0.94);

        waitForStart();

        if (isStopRequested()) return;
        if (opModeIsActive()) {
            // Put run blocks here.
            ClawHand.setPosition(0.3);
            Airplane.setPosition(0.7);
            IntakeStopper.setPosition(0.23);

            while (opModeIsActive()) {
                double y = -gamepad1.left_stick_y; // Remember, Y stick value is reversed
                double x = gamepad1.left_stick_x * 1.1; // Counteract imperfect strafing
                double rx = gamepad1.right_stick_x;

                // Denominator is the largest motor power (absolute value) or 1
                // This ensures all the powers maintain the same ratio,
                // but only if at least one is out of the range [-1, 1]
                double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
                double frontLeftPower = (y + x + rx) / denominator;
                double backLeftPower = (y - x + rx) / denominator;
                double frontRightPower = (y - x - rx) / denominator;
                double backRightPower = (y + x - rx) / denominator;

                frontLeftMotor.setPower(frontLeftPower);
                backLeftMotor.setPower(backLeftPower);
                frontRightMotor.setPower(frontRightPower);
                backRightMotor.setPower(backRightPower);

                frontLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                backLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                frontRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                backRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
                // Claw Intake Down
                if (gamepad2.a) {
                    ClawIntLeft.setDirection(DcMotor.Direction.REVERSE);
                    ClawIntRight.setDirection(DcMotor.Direction.REVERSE);
                    ClawIntRight.setPower(0.3);
                    ClawIntLeft.setPower(0.3);
                    sleep(100);
                    ClawIntLeft.setPower(0);
                    ClawIntRight.setPower(0);
                } else if (gamepad2.left_stick_button) {
                    ClawIntLeft.setDirection(DcMotor.Direction.REVERSE);
                    ClawIntRight.setDirection(DcMotor.Direction.FORWARD);
                    ClawIntLeft.setPower(1);
                    ClawIntRight.setPower(1);
                    sleep(100);
                    ClawIntRight.setPower(0);
                    ClawIntLeft.setPower(0);
                }// Claw Intake Up
                if (gamepad2.y) {
                    ClawIntLeft.setDirection(DcMotor.Direction.FORWARD);
                    ClawIntRight.setDirection(DcMotor.Direction.REVERSE);
                    ClawIntRight.setPower(1);
                    ClawIntLeft.setPower(1);
                    sleep(100);
                    ClawIntRight.setPower(0);
                    ClawIntLeft.setPower(0);
                }
                // Claw Hand
                if (gamepad2.x) {
                    ClawHand.setPosition(1);
                }
                // Claw Hand
                if (gamepad2.b) {
                    ClawHand.setPosition(0.4);
                }
                // Claw Open
                if (gamepad2.right_bumper) {
                    ClawPos.setPosition(0.94);
                }
                // Claw Close
                if (gamepad2.left_bumper) {
                    ClawPos.setPosition(0.8);
                }
                // Airplane System Let go
                if (0 < gamepad1.right_trigger) {
                    Airplane.setPosition(0.5);
                }
                // Airplane System Pos
                if (0 < gamepad1.left_trigger) {
                    Airplane.setPosition(0.7);
                }
                // Stopper Open
                if (0 < gamepad2.left_trigger) {
                    IntakeStopper.setPosition(0.6);
                }
                // Stopper Close
                if (0 < gamepad2.right_trigger) {
                    IntakeStopper.setPosition(0.23);
                }
            }
        }
    }
}
