{ def ack(m: Int, n: Int): Int =
    if (m == 0) n + 1
    else if (n == 0) ack(m - 1, 1)
    else ack(m - 1, ack(m, n - 1));
  ack(a, b)
}
