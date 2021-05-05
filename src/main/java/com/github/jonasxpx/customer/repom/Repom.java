package com.github.jonasxpx.customer.repom;

import com.github.jonasxpx.Dates;
import com.github.jonasxpx.customer.generic.Credit;
import com.github.jonasxpx.customer.generic.Debit;
import com.github.jonasxpx.customer.generic.MonthlyPayment;
import com.github.jonasxpx.customer.generic.Ticket;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.github.jonasxpx.customer.repom.TipoLancamento.*;
import static java.lang.String.format;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class Repom {

    private final List<RepomModal> modals;

    private final List<Credit> credits;
    private final List<Debit> debits;
    private final List<Ticket> tickets;
    private final List<MonthlyPayment> monthlyPayments;

    private Dates datas;

    public Repom(List<RepomModal> modals) {
        this.modals = modals;
        this.credits = new ArrayList<>();
        this.monthlyPayments = new ArrayList<>();
        this.debits = new ArrayList<>();
        this.tickets = new ArrayList<>();

        modals.forEach(this::includeCreditsValorPedadio);
        modals.forEach(this::includeCreditsEstorno);
        modals.forEach(this::includeMensalidade);
        modals.forEach(this::includeDebits);
        modals.forEach(this::includeTickets);

        if(!modals.isEmpty()) {
            defineDatasDaFatura();
        }
    }

    private void includeTickets(RepomModal repomModal) {
        if (Objects.isNull(repomModal.getTipo())) {
            return;
        }

        if(!repomModal.getTipo().equals(PASSAGEM)) {
            return;
        }

        Ticket ticket = Ticket.builder()
                .value(repomModal.getValorPassagem())
                .build();

        tickets.add(ticket);
    }

    private void includeDebits(RepomModal repomModal) {
        if (Objects.isNull(repomModal.getTipo())) {
            return;
        }

        if(!repomModal.getTipo().equals(RECARGA)) {
            return;
        }

        Debit debit = Debit.builder()
                .value(BigDecimal.valueOf(repomModal.getValor()))
                .build();

        debits.add(debit);
    }

    private void defineDatasDaFatura() {
        setDatas(modals.get(0).getFiltro());
    }

    private void includeCreditsValorPedadio(RepomModal repomModal) {
        if (Optional.ofNullable(repomModal.getValorValePedagio()).orElse(-1D) > 0) {
            Credit creditoVp = Credit.builder()
                    .bill(Math.round(Math.random() * 100000))
                    .date(repomModal.getDataPassagem())
                    .description("Credito VP")
                    .isValePedagio(true)
                    .plate(repomModal.getPlaca())
                    .plaza(Long.valueOf(repomModal.getIdPlaca()))
                    .value(BigDecimal.valueOf(repomModal.getValorValePedagio()))
                    .build();

            credits.add(creditoVp);
        }
    }

    private void includeCreditsEstorno(RepomModal repomModal) {
        if (Objects.isNull(repomModal.getTipo())) {
            return;
        }

        if (!repomModal.getTipo().equals(ESTORNO_PASSAGEM)) {
            return;
        }

        Credit creditEstorno = Credit.builder()
                .bill(Math.round(Math.random() * 100000))
                .date(repomModal.getDataPassagem())
                .plate(repomModal.getPlaca())
                .description("Estorno de Passagem")
                .value(repomModal.getValorPassagem())
                .build();
        credits.add(creditEstorno);
    }

    private void includeMensalidade(RepomModal repomModal) {
        if (Objects.isNull(repomModal.getTipo())) {
            return;
        }

        if (!repomModal.getTipo().equals(MENSALIDADE)) {
            return;
        }

        if(repomModal.getValor() == 0) {
            return;
        }

        MonthlyPayment monthlyPayment = MonthlyPayment.builder()
                .bill(Math.round(Math.random() * 100000))
                .date(LocalDateTime.now())
                .plate(repomModal.getPlaca())
                .referenceDate(repomModal.getDataPassagem())
                .value(repomModal.getValorPassagem())
                .build();

        monthlyPayments.add(monthlyPayment);
    }



    private BigDecimal getTotalMonthly() {
        return monthlyPayments.stream()
                .map(MonthlyPayment::getValue)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }

    private BigDecimal getTotalDebits() {
        return debits.stream()
                .map(Debit::getValue)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }

    private BigDecimal getTotalCredits() {
        return credits.stream()
                .map(Credit::getValue)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }

    private BigDecimal getTotalTickets() {
        return tickets.stream()
                .map(Ticket::getValue)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }


    @Override
    public String toString() {
        return "Repom{" +
                "Vencimento=" + getDatas().getEnd() +
                format(", (%s) Total Passagens=R$ %s", tickets.size(), getTotalTickets()) +
                format(", (%s) Total Mensalidades=R$ %s", monthlyPayments.size(), getTotalMonthly()) +
                format(", (%s) Total Creditos=R$ %s", credits.size(), getTotalCredits()) +
                format(", (%s) Total Debitos=R$ %s", debits.size(), getTotalDebits()) +
                '}';
    }
}
