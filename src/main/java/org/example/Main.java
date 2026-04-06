package org.example;

/*
    Вагон. Возможности: получить или изменить различную информацию
    о вагоне: регистрационный номер вагона, пункт назначения, владелец,
    статус (загрузка, разгрузка, в пути, в ремонте, готов к отправке, требуется ремонт);
    узнать снаряженную массу; отправить в пункт назначения; обслужить; отремонтировать;
    загрузить; разгрузить; узнать хрупкость/ценность груза.
    Добавить дополнительные возможности для грузового и пассажирского вагонов.
 */

import java.util.Scanner;

/**
 * Интерфейс, описывающий базовые возможности вагона.
 */
interface Wagon {
    // Получить или изменить информацию о вагоне
    String getRegistrationNumber();
    void setRegistrationNumber(String number);
    String getDestination();
    void setDestination(String destination);
    String getOwner();
    void setOwner(String owner);
    String getStatus();
    void setStatus(String status);

    // Узнать снаряженную массу (в тоннах)
    double getMass();

    // Отправить в пункт назначения
    void sendToDestination();

    // Обслужить
    void service();

    // Отремонтировать
    void repair();

    // Загрузить груз (для грузового) / посадить пассажиров (для пассажирского)
    void load(int amount);

    // Разгрузить / высадить
    void unload(int amount);

    // Узнать хрупкость/ценность груза (для грузового) / комфортность (для пассажирского)
    String getCargoFragility();  // для грузового – хрупкость, для пассажирского – уровень комфорта
    double getCargoValue();      // ценность груза / стоимость билета и т.п.
}

/**
 * Грузовой вагон – дополнительные возможности.
 */
class FreightWagon implements Wagon {
    private String registrationNumber;
    private String destination;
    private String owner;
    private String status;       // загрузка, разгрузка, в пути, в ремонте, готов к отправке, требуется ремонт
    private double mass;         // снаряженная масса
    private String cargoType;    // тип груза
    private double maxLoad;      // максимальная загрузка (тонн)
    private double currentLoad;  // текущая загрузка
    private String fragility;    // хрупкость (высокая, средняя, низкая)
    private double cargoValue;   // ценность груза (условно)

    public FreightWagon(String number, String destination, String owner, double mass,
                        String cargoType, double maxLoad, String fragility, double cargoValue) {
        this.registrationNumber = number;
        this.destination = destination;
        this.owner = owner;
        this.status = "готов к отправке";
        this.mass = mass;
        this.cargoType = cargoType;
        this.maxLoad = maxLoad;
        this.currentLoad = 0;
        this.fragility = fragility;
        this.cargoValue = cargoValue;
    }

    // Реализация методов интерфейса
    @Override
    public String getRegistrationNumber() { return registrationNumber; }
    @Override
    public void setRegistrationNumber(String number) { this.registrationNumber = number; }
    @Override
    public String getDestination() { return destination; }
    @Override
    public void setDestination(String destination) { this.destination = destination; }
    @Override
    public String getOwner() { return owner; }
    @Override
    public void setOwner(String owner) { this.owner = owner; }
    @Override
    public String getStatus() { return status; }
    @Override
    public void setStatus(String status) { this.status = status; }
    @Override
    public double getMass() { return mass; }

    @Override
    public void sendToDestination() {
        if (status.equals("готов к отправке") && currentLoad <= maxLoad) {
            status = "в пути";
            System.out.println("Грузовой вагон " + registrationNumber + " отправлен в " + destination);
        } else {
            System.out.println("Невозможно отправить: статус " + status + " или превышена загрузка");
        }
    }

    @Override
    public void service() {
        System.out.println("Грузовой вагон " + registrationNumber + " проходит техобслуживание");
        status = "готов к отправке";
    }

    @Override
    public void repair() {
        System.out.println("Грузовой вагон " + registrationNumber + " ремонтируется");
        status = "требуется ремонт";
    }

    @Override
    public void load(int amount) {
        double loadAmount = amount; // в тоннах
        if (currentLoad + loadAmount <= maxLoad) {
            currentLoad += loadAmount;
            status = "загрузка";
            System.out.println("Загружено " + loadAmount + " т. Текущая загрузка: " + currentLoad + " т");
        } else {
            System.out.println("Перегруз! Максимум " + maxLoad + " т");
        }
    }

    @Override
    public void unload(int amount) {
        double unloadAmount = amount;
        if (currentLoad - unloadAmount >= 0) {
            currentLoad -= unloadAmount;
            status = "разгрузка";
            System.out.println("Разгружено " + unloadAmount + " т. Осталось: " + currentLoad + " т");
        } else {
            System.out.println("Невозможно разгрузить больше, чем загружено");
        }
    }

    @Override
    public String getCargoFragility() { return fragility; }
    @Override
    public double getCargoValue() { return cargoValue; }

    // Дополнительные методы для грузового вагона
    public String getCargoType() { return cargoType; }
    public void setCargoType(String cargoType) { this.cargoType = cargoType; }
    public double getMaxLoad() { return maxLoad; }
    public double getCurrentLoad() { return currentLoad; }

    @Override
    public String toString() {
        return String.format("Грузовой вагон №%s, пункт назначения: %s, владелец: %s, статус: %s, масса: %.1f т, тип груза: %s, загрузка: %.1f/%.1f т, хрупкость: %s, ценность: %.2f",
                registrationNumber, destination, owner, status, mass, cargoType, currentLoad, maxLoad, fragility, cargoValue);
    }
}

/**
 * Пассажирский вагон – дополнительные возможности.
 */
class PassengerWagon implements Wagon {
    private String registrationNumber;
    private String destination;
    private String owner;
    private String status;
    private double mass;
    private int seatCount;          // количество мест
    private int passengerCount;     // текущее количество пассажиров
    private String comfortLevel;    // уровень комфорта (эконом, бизнес, люкс)
    private double ticketPrice;     // цена билета (для ценности)
    private boolean hasAirConditioning;

    public PassengerWagon(String number, String destination, String owner, double mass,
                          int seatCount, String comfortLevel, double ticketPrice, boolean hasAirConditioning) {
        this.registrationNumber = number;
        this.destination = destination;
        this.owner = owner;
        this.status = "готов к отправке";
        this.mass = mass;
        this.seatCount = seatCount;
        this.passengerCount = 0;
        this.comfortLevel = comfortLevel;
        this.ticketPrice = ticketPrice;
        this.hasAirConditioning = hasAirConditioning;
    }

    @Override
    public String getRegistrationNumber() { return registrationNumber; }
    @Override
    public void setRegistrationNumber(String number) { this.registrationNumber = number; }
    @Override
    public String getDestination() { return destination; }
    @Override
    public void setDestination(String destination) { this.destination = destination; }
    @Override
    public String getOwner() { return owner; }
    @Override
    public void setOwner(String owner) { this.owner = owner; }
    @Override
    public String getStatus() { return status; }
    @Override
    public void setStatus(String status) { this.status = status; }
    @Override
    public double getMass() { return mass; }

    @Override
    public void sendToDestination() {
        if (status.equals("готов к отправке")) {
            status = "в пути";
            System.out.println("Пассажирский вагон " + registrationNumber + " отправлен в " + destination);
        } else {
            System.out.println("Невозможно отправить: статус " + status);
        }
    }

    @Override
    public void service() {
        System.out.println("Пассажирский вагон " + registrationNumber + " проходит уборку и техобслуживание");
        status = "готов к отправке";
    }

    @Override
    public void repair() {
        System.out.println("Пассажирский вагон " + registrationNumber + " ремонтируется");
        status = "требуется ремонт";
    }

    @Override
    public void load(int amount) {
        int passengers = amount;
        if (passengerCount + passengers <= seatCount) {
            passengerCount += passengers;
            status = "посадка";
            System.out.println("Посажено " + passengers + " пассажиров. Всего: " + passengerCount);
        } else {
            System.out.println("Не хватает мест! Свободно мест: " + (seatCount - passengerCount));
        }
    }

    @Override
    public void unload(int amount) {
        int passengers = amount;
        if (passengerCount - passengers >= 0) {
            passengerCount -= passengers;
            status = "высадка";
            System.out.println("Высажено " + passengers + " пассажиров. Осталось: " + passengerCount);
        } else {
            System.out.println("Столько пассажиров нет в вагоне");
        }
    }

    @Override
    public String getCargoFragility() { return comfortLevel; } // для пассажирского – уровень комфорта
    @Override
    public double getCargoValue() { return ticketPrice; }      // ценность – цена билета

    // Дополнительные методы для пассажирского вагона
    public int getSeatCount() { return seatCount; }
    public int getPassengerCount() { return passengerCount; }
    public boolean hasAirConditioning() { return hasAirConditioning; }
    public void setAirConditioning(boolean enabled) { this.hasAirConditioning = enabled; }

    @Override
    public String toString() {
        return String.format("Пассажирский вагон №%s, пункт назначения: %s, владелец: %s, статус: %s, масса: %.1f т, мест: %d, пассажиров: %d, комфорт: %s, цена билета: %.2f, кондиционер: %s",
                registrationNumber, destination, owner, status, mass, seatCount, passengerCount,
                comfortLevel, ticketPrice, hasAirConditioning ? "есть" : "нет");
    }
}

/**
 * Главный класс с меню для демонстрации работы.
 */
public class Main {
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("=== Управление вагонами ===");
        // Создаём несколько вагонов для демонстрации
        FreightWagon freight = new FreightWagon("FG-123", "Москва", "РЖД", 22.5,
                "Уголь", 70.0, "средняя", 150000.0);
        PassengerWagon passenger = new PassengerWagon("PS-456", "Санкт-Петербург", "ФПК", 45.0,
                54, "эконом", 1200.0, true);

        while (true) {
            System.out.println("\nВыберите вагон:");
            System.out.println("1 - Грузовой вагон");
            System.out.println("2 - Пассажирский вагон");
            System.out.println("0 - Выход");
            System.out.print("Ваш выбор: ");
            int choice = scanner.nextInt();
            scanner.nextLine();
            if (choice == 0) break;

            if (choice == 1) {
                menuForWagon(freight, "Грузовой");
            } else if (choice == 2) {
                menuForWagon(passenger, "Пассажирский");
            } else {
                System.out.println("Неверный выбор");
            }
        }

        System.out.println("\nРазработчик: Парамонов С.В.");
        System.out.println("Дата получения: 07.03.2026");
        System.out.println("Дата сдачи: 04.04.2026");
    }

    private static void menuForWagon(Wagon wagon, String type) {
        while (true) {
            System.out.println("\n--- " + type + " вагон ---");
            System.out.println("Текущая информация: " + wagon);
            System.out.println("1 - Изменить регистрационный номер");
            System.out.println("2 - Изменить пункт назначения");
            System.out.println("3 - Изменить владельца");
            System.out.println("4 - Изменить статус");
            System.out.println("5 - Узнать снаряженную массу");
            System.out.println("6 - Отправить в пункт назначения");
            System.out.println("7 - Обслужить");
            System.out.println("8 - Отремонтировать");
            System.out.println("9 - Загрузить (посадить)");
            System.out.println("10 - Разгрузить (высадить)");
            System.out.println("11 - Узнать хрупкость/комфортность");
            System.out.println("12 - Узнать ценность груза/билета");
            if (wagon instanceof FreightWagon) {
                System.out.println("13 - Изменить тип груза (грузовой)");
                System.out.println("14 - Показать максимальную загрузку");
            } else if (wagon instanceof PassengerWagon) {
                System.out.println("13 - Включить/выключить кондиционер");
                System.out.println("14 - Показать количество мест");
            }
            System.out.println("0 - Назад");
            System.out.print("Выбор: ");
            int cmd = scanner.nextInt();
            scanner.nextLine();
            if (cmd == 0) break;

            switch (cmd) {
                case 1:
                    System.out.print("Новый номер: ");
                    wagon.setRegistrationNumber(scanner.nextLine());
                    break;
                case 2:
                    System.out.print("Новый пункт назначения: ");
                    wagon.setDestination(scanner.nextLine());
                    break;
                case 3:
                    System.out.print("Новый владелец: ");
                    wagon.setOwner(scanner.nextLine());
                    break;
                case 4:
                    System.out.print("Новый статус (загрузка, разгрузка, в пути, в ремонте, готов к отправке, требуется ремонт): ");
                    wagon.setStatus(scanner.nextLine());
                    break;
                case 5:
                    System.out.println("Снаряженная масса: " + wagon.getMass() + " т");
                    break;
                case 6:
                    wagon.sendToDestination();
                    break;
                case 7:
                    wagon.service();
                    break;
                case 8:
                    wagon.repair();
                    break;
                case 9:
                    System.out.print("Количество (тонн или пассажиров): ");
                    int amount = scanner.nextInt();
                    wagon.load(amount);
                    break;
                case 10:
                    System.out.print("Количество (тонн или пассажиров): ");
                    int unloadAmount = scanner.nextInt();
                    wagon.unload(unloadAmount);
                    break;
                case 11:
                    System.out.println("Хрупкость/комфортность: " + wagon.getCargoFragility());
                    break;
                case 12:
                    System.out.println("Ценность груза/билета: " + wagon.getCargoValue());
                    break;
                case 13:
                    if (wagon instanceof FreightWagon) {
                        System.out.print("Новый тип груза: ");
                        String cargoType = scanner.nextLine();
                        ((FreightWagon) wagon).setCargoType(cargoType);
                    } else if (wagon instanceof PassengerWagon) {
                        boolean current = ((PassengerWagon) wagon).hasAirConditioning();
                        ((PassengerWagon) wagon).setAirConditioning(!current);
                        System.out.println("Кондиционер " + (!current ? "включён" : "выключен"));
                    }
                    break;
                case 14:
                    if (wagon instanceof FreightWagon) {
                        System.out.println("Максимальная загрузка: " + ((FreightWagon) wagon).getMaxLoad() + " т");
                    } else if (wagon instanceof PassengerWagon) {
                        System.out.println("Количество мест: " + ((PassengerWagon) wagon).getSeatCount());
                    }
                    break;
                default:
                    System.out.println("Неверная команда");
            }
        }
    }
}