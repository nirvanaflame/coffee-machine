package machine

class Machine() {
  private constructor(water: Int, milk: Int, beans: Int, cups: Int, money: Int) : this() {
    this.state = State(water, milk, beans, cups, money)
  }

  enum class Coffee(val cost: Int, val water: Int, val milk: Int, val beans: Int) {
    ESPRESSO(4, 250, 0, 16),
    LATTE(7, 350, 75, 20),
    CAPPUCCINO(6, 200, 100, 12)
  }

  inner class State(var water: Int, var milk: Int, var beans: Int, var cups: Int, var money: Int) {
    fun consumeWater(water: Int) {
      if (this.water >= water) this.water -= water
      else throw IllegalStateException("Sorry, not enough water!")
    }

    fun consumeMilk(milk: Int) {
      if (this.milk >= milk) this.milk -= milk
      else throw IllegalStateException("Sorry, not enough milk!")
    }

    fun consumeBeans(beans: Int) {
      if (this.beans >= beans) this.beans -= beans
      else throw IllegalStateException("Sorry, not enough beans!")
    }

    fun consumeCup() {
      if (cups >= 1) cups -= 1
      else throw IllegalStateException("Sorry, not enough cups!")
    }

    fun updateMoney(money: Int) {
      this.money += money
    }
  }

  companion object Factory {
    fun init(water: Int, milk: Int, beans: Int, cups: Int, money: Int): Machine =
      Machine(water, milk, beans, cups, money)
  }

  private var state = State(0, 0, 0, 0, 0)

  fun printState() {
    println(
      """
        The coffee machine has:
        ${this.state.water} ml of water
        ${this.state.milk} ml of milk
        ${this.state.beans} g of coffee beans
        ${this.state.cups} disposable cups
        $${this.state.money} of money
        """.trimIndent()
    )
  }

  fun sellCoffee(choice: String) {
    when (choice) {
      "1" -> makeCoffee(Coffee.ESPRESSO)
      "2" -> makeCoffee(Coffee.LATTE)
      "3" -> makeCoffee(Coffee.CAPPUCCINO)
      "back" -> return
    }
  }

  private fun makeCoffee(coffee: Coffee) {
    val copyState = state.copy()
    try {
      state.consumeWater(coffee.water)
      state.consumeMilk(coffee.milk)
      state.consumeBeans(coffee.beans)
      state.consumeCup()
      state.updateMoney(coffee.cost)
    } catch (e: IllegalStateException) {
      println(e.message)
      state = copyState
    }
  }

  fun topUp(a: IntArray) {
    state.water += a[0]
    state.milk += a[1]
    state.beans += a[2]
    state.cups += a[3]
  }

  fun giveMoney() {
    println("I gave you $${state.money}")
    state.money = 0
  }

  private fun State.copy(): State {
    return State(this.water, this.milk, this.beans, this.cups, this.money)
  }
}

fun main() {
  val machine = Machine.init(400, 540, 120, 9, 550)

  while (true) {
    when (readPrompt("Write action (buy, fill, take, remaining, exit):")) {
      "buy" -> machine.buyCoffee()
      "fill" -> machine.fill()
      "take" -> machine.giveMoney()
      "remaining" -> machine.printState()
      "exit" -> break
    }
  }
}

fun Machine.buyCoffee() {
  val choice = readPrompt(
    "What do you want to buy? " +
        "1 - espresso, 2 - latte, 3 - cappuccino, back - to main menu:"
  )
  this.sellCoffee(choice)
}

fun Machine.fill() {
  val array = intArrayOf(
    readInt("Write how many ml of water you want to add:"),
    readInt("Write how many ml of milk you want to add:"),
    readInt("Write how many grams of coffee beans you want to add:"),
    readInt("Write how many disposable cups you want to add:")
  )
  this.topUp(array)
}

fun readInt(prompt: String): Int {
  return readPrompt(prompt).toInt()
}

fun readPrompt(prompt: String): String {
  println(prompt)
  return readln()
}
