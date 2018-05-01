package com.generic.retailer;

import com.generic.retailer.domain.ProductFactory;
import com.generic.retailer.exceptions.ProductNotFoundException;
import com.generic.retailer.service.RetailerService;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import static java.util.Objects.requireNonNull;

public final class Cli implements AutoCloseable {


  public static Cli create(String prompt, BufferedReader reader, BufferedWriter writer, RetailerService retailerService, LocalDate date) {
    requireNonNull(prompt);
    requireNonNull(reader);
    requireNonNull(writer);
    return new Cli(prompt, reader, writer, retailerService, date);
  }

  public static Cli create(BufferedReader reader, BufferedWriter writer, RetailerService retailerService) {
    return new Cli(">", reader, writer, retailerService, LocalDate.now());
  }

  private static final Predicate<String> WHITESPACE = Pattern.compile("^\\s{0,}$").asPredicate();

  private final String prompt;
  private final BufferedReader reader;
  private final BufferedWriter writer;
  private final LocalDate date;
  private final RetailerService retailerService;

  private Cli(String prompt, BufferedReader reader, BufferedWriter writer, RetailerService retailerService, LocalDate date) {
    this.prompt = prompt;
    this.reader = reader;
    this.writer = writer;
    this.retailerService = retailerService;
    this.date = date;
  }

  private void prompt() throws IOException {
    writeLine(prompt);
  }

  private Optional<String> readLine() throws IOException {
    String line = reader.readLine();
    return line == null || WHITESPACE.test(line) ? Optional.empty() : Optional.of(line);
  }

  private void writeLine(String line) throws IOException {
    writer.write(line);
    writer.newLine();
    writer.flush();
  }

  public void run() throws IOException {
    writeLine("What would you like to buy?");
    prompt();
    Optional<String> line = readLine();
    while (line.isPresent()) {
      try {
        retailerService.addProductToTrolley(ProductFactory.getProduct(line.get()));
        writeLine("Would you like anything else?");
      }
      catch (ProductNotFoundException e) {
        writeLine("The product type you entered is not available. If you would like to add another product, please enter either: Book, CD, or DVD.");
      }
      prompt();
      line = readLine();
    }
    writeLine(String.format("Thank you for visiting Generic Retailer, your total is %s", "£" + String.format("%.2f", retailerService.getGrandTotalWithReductions(date))));
    writeLine(retailerService.getReceipt(date));
  }

  @Override
  public void close() throws Exception {
      reader.close();
      writer.close();
  }

}
