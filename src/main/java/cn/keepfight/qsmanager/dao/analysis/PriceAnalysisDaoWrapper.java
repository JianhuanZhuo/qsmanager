package cn.keepfight.qsmanager.dao.analysis;

import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class PriceAnalysisDaoWrapper {
    private StringProperty serial = new SimpleStringProperty();
    private ObjectProperty<BigDecimal> cost = new SimpleObjectProperty<>(new BigDecimal(0));
    private ObjectProperty<BigDecimal> price = new SimpleObjectProperty<>(new BigDecimal(0));
    private ObjectProperty<BigDecimal> profit = new SimpleObjectProperty<>(new BigDecimal(0));
    private ObjectProperty<BigDecimal> profitRate = new SimpleObjectProperty<>(new BigDecimal(0));
    private ObjectProperty<BigDecimal> totalProfit = new SimpleObjectProperty<>(new BigDecimal(0));
    private ObjectProperty<BigDecimal> total = new SimpleObjectProperty<>();
    private Map<Integer, BigDecimal> monthMapCount = new HashMap<>();

    {
        profit.bind(new ObjectBinding<BigDecimal>() {
            {
                bind(cost, price);
            }

            @Override
            protected BigDecimal computeValue() {
                try {
                    return price.get().subtract(cost.get());
                } catch (Exception e) {
                    return new BigDecimal(0);
                }
            }
        });
        profitRate.bind(new ObjectBinding<BigDecimal>() {
            {
                bind(price, profit);
            }

            @Override
            protected BigDecimal computeValue() {
                try {
                    return profit.get().divide(price.get(), 5, BigDecimal.ROUND_UP);
                } catch (Exception e) {
                    return new BigDecimal(0);
                }
            }
        });
        totalProfit.bind(new ObjectBinding<BigDecimal>() {
            {
                bind(profit, total);
            }

            @Override
            protected BigDecimal computeValue() {
                try {
                    return profit.get().multiply(total.get());
                } catch (Exception e) {
                    return new BigDecimal(0);
                }
            }
        });
    }

    public String getSerial() {
        return serial.get();
    }

    public StringProperty serialProperty() {
        return serial;
    }

    public void setSerial(String serial) {
        this.serial.set(serial);
    }

    public BigDecimal getCost() {
        return cost.get();
    }

    public ObjectProperty<BigDecimal> costProperty() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost.set(cost);
    }

    public BigDecimal getPrice() {
        return price.get();
    }

    public ObjectProperty<BigDecimal> priceProperty() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price.set(price);
    }

    public BigDecimal getTotalProfit() {
        return totalProfit.get();
    }

    public ObjectProperty<BigDecimal> totalProfitProperty() {
        return totalProfit;
    }

    public void setTotalProfit(BigDecimal totalProfit) {
        this.totalProfit.set(totalProfit);
    }

    public BigDecimal getProfit() {
        return profit.get();
    }

    public ObjectProperty<BigDecimal> profitProperty() {
        return profit;
    }

    public void setProfit(BigDecimal profit) {
        this.profit.set(profit);
    }

    public BigDecimal getProfitRate() {
        return profitRate.get();
    }

    public ObjectProperty<BigDecimal> profitRateProperty() {
        return profitRate;
    }

    public void setProfitRate(BigDecimal profitRate) {
        this.profitRate.set(profitRate);
    }

    public BigDecimal getTotal() {
        return total.get();
    }

    public ObjectProperty<BigDecimal> totalProperty() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total.set(total);
    }

    public Map<Integer, BigDecimal> getMonthMapCount() {
        return monthMapCount;
    }

    public void setMonthMapCount(Map<Integer, BigDecimal> monthMapCount) {
        this.monthMapCount = monthMapCount;
    }

    public ObjectProperty<BigDecimal> getPropertyByYM(int month) {
        return new SimpleObjectProperty<>(monthMapCount.getOrDefault(month, new BigDecimal(0)));
    }
}
