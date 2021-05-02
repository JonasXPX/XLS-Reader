package com.github.jonasxpx.customer.repom;

import com.github.jonasxpx.customer.generic.Credit;
import com.github.jonasxpx.customer.generic.MonthlyPayment;
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

import static com.github.jonasxpx.customer.repom.TipoLancamento.ESTORNO_PASSAGEM;
import static com.github.jonasxpx.customer.repom.TipoLancamento.PASSAGEM;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class Repom {

    private List<RepomModal> modals;
    private List<Credit> credits;
    private List<MonthlyPayment> monthlyPayments;

    public Repom(List<RepomModal> modals) {
        this.modals = modals;
        this.credits = new ArrayList<>();
        this.monthlyPayments = new ArrayList<>();

        modals.forEach(this::includeCreditsValorPedadio);
        modals.forEach(this::includeCreditsEstorno);
        modals.forEach(this::includeMensalidade);
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
                .value(BigDecimal.valueOf(repomModal.getValorPassagem()))
                .build();
        credits.add(creditEstorno);
    }

    private void includeMensalidade(RepomModal repomModal) {
        if (Objects.isNull(repomModal.getTipo())) {
            return;
        }

        if (!repomModal.getTipo().equals(PASSAGEM)) {
            return;
        }

        if(repomModal.getValor() == 0) {
            return;
        }

        MonthlyPayment monthlyPayment = MonthlyPayment.builder()
                .bill(Math.round(Math.random() * 100000))
                .emission(LocalDateTime.now())
                .plate(repomModal.getPlaca())
                .referenceDate(repomModal.getDataPassagem())
                .value(BigDecimal.valueOf(repomModal.getValorPassagem()).negate())
                .build();

        monthlyPayments.add(monthlyPayment);
    }


    @Override
    public String toString() {
        return "Repom{" +
                "modals=" + modals.size() +
                ", credits=" + credits.size() +
                ", mensalidades=" + monthlyPayments.size() +
                '}';
    }
}
