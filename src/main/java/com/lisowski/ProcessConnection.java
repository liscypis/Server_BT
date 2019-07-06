package com.lisowski;

import java.awt.*;
import java.awt.event.InputEvent;
import java.io.*;

import javax.microedition.io.StreamConnection;

import static java.awt.event.KeyEvent.*;


public class ProcessConnection extends Thread {
    private StreamConnection mConnection;
    private Robot robot;

    public ProcessConnection(StreamConnection connection) {
        try {
            robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }
        mConnection = connection;
    }

    @Override
    public void run() {
        try {
            InputStream inputStream = mConnection.openInputStream();
            System.out.println("Czekam na dane");
            byte[] array = new byte[7];
            int numBytes = 0;
            while (true) {
                try {
                    numBytes = inputStream.read(array);

                    if (numBytes > 0) {
//                        System.out.println(array[0]+ " " + array[1] + " " + array[2] + " " + array[3] + " " + array[4] + " " + array[5] + " " + array[6]);
//                        System.out.println(numBytes);

                        checkMessage(array);
                        if (numBytes == 1) {
                            System.out.println("FINITO");
                            break;
                        }
                    }
                } catch (EOFException e) {
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void checkMessage(byte[] message) {
        if (message[0] != 0 || message[1] != 0)
            processMouseData(message);

        if (message[2] != 0)
            processPressMouse(message);

        if (message[5] != 0)
            processKeyboardData(message[5]);

        if (message[6] != 0)
            processWheel(message[6]);
    }

    private void processWheel(byte b) {
        robot.delay(30);
        robot.mouseWheel(b);
    }

    private void processPressMouse(byte[] message) {
        if (message[3] == 1 && message[4] == 1) { //  jedno klikniecie
            if (message[2] == 1) {
                robot.mousePress(InputEvent.BUTTON1_MASK);
                robot.mouseRelease(InputEvent.BUTTON1_MASK);
            }
            if (message[2] == 3) {
                robot.mousePress(InputEvent.BUTTON3_MASK);
                robot.mouseRelease(InputEvent.BUTTON3_MASK);
            }
        }
        if (message[3] == 1 && message[4] == 0) { // trzymamy LPM
            robot.mousePress(InputEvent.BUTTON1_MASK);
        }
        if (message[3] == 0 && message[4] == 1) { // puszczamy lpm
            robot.mouseRelease(InputEvent.BUTTON1_MASK);
        }
    }

    private void processMouseData(byte[] array) {
        PointerInfo pi = MouseInfo.getPointerInfo();
        Point p = pi.getLocation();
        int x = p.x;
        int y = p.y;
        x += array[0];
        y += array[1];
//        robot.delay(5);
        robot.mouseMove(x, y);
//        System.out.println(x + " " + y);
    }

    private void processKeyboardData(int key) {
        switch (key) {
            case 33:
                typeWithShift(robot, VK_1); // !
                break;
            case 64:
                typeWithShift(robot, VK_2); // @
                break;
            case 35:
                typeWithShift(robot, VK_3); // #
                break;
            case 36:
                typeWithShift(robot, VK_4); // $
                break;
            case 37:
                typeWithShift(robot, VK_5); // %
                break;
            case 40:
                typeWithShift(robot, VK_9); // (
                break;
            case 41:
                typeWithShift(robot, VK_0); // !
                break;
            case 43:
                typeWithShift(robot, VK_EQUALS); // +
                break;
            case 42:
                typeWithShift(robot, VK_8); // *
                break;
            case 39:
                robot.keyPress(VK_QUOTE); // '
                break;
            case 94:
                typeWithShift(robot, VK_6); // ^
                break;
            case 95:
                typeWithShift(robot, VK_MINUS); // _
                break;
            case 38:
                typeWithShift(robot, VK_7); // &
                break;
            case 63:
                typeWithShift(robot, VK_SLASH); // ?
                break;
            case 34:
                typeWithShift(robot, VK_QUOTE); // "
                break;
            case 123:
                typeWithShift(robot, VK_OPEN_BRACKET); // {
                break;
            case 125:
                typeWithShift(robot, VK_CLOSE_BRACKET); // }
                break;
            case 60:
                typeWithShift(robot, VK_COMMA); // <
                break;
            case 62:
                typeWithShift(robot, VK_PERIOD); // >
                break;
            case 124:
                typeWithShift(robot, VK_BACK_SLASH); // |
                break;
            case 96:
                robot.keyPress(VK_BACK_QUOTE); // `
                break;
            case 126:
                typeWithShift(robot, VK_BACK_QUOTE); // ~
                break;
            case 58:
                typeWithShift(robot, VK_SEMICOLON); // :
                break;
            default:
                if (key >= 97 && key <= 122) {
                    robot.keyPress(key - 32);
                } else if (key >= 65 && key <= 90) {
                    typeWithShift(robot, key);
                } else
                    robot.keyPress(key);
        }
    }

    private void typeWithShift(Robot robot, int key) {
        robot.keyPress(VK_SHIFT);
        robot.keyPress(key);
        robot.keyRelease(VK_SHIFT);
    }
}