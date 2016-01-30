package com.trading;

import org.junit.BeforeClass;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class FixmlMessageParserSpec {

    private static final String FIXML_ALLOCATION_REPORT_MESSAGE = "<FIXML><AllocRpt TransTyp=\"0\" RptID=\"%s\" GrpID=\"1234567\" AvgPxGrpID=\"AP101\" Stat=\"3\" BizDt=\"2016-06-03\" RptTyp=\"2\" Qty=\"200\" AvgPxInd=\"2\" Side=\"1\" TrdTyp=\"0\" TrdSubTyp=\"5\" AvgPx=\"57.5054673\" TrdDt=\"2016-06-03\" RndPx=\"57.51\" GrpQty=\"350\" RemQty=\"150\" InptDev=\"API\"><Hdr SID=\"ICE\" TID=\"GUF\"/><Instrmt ID=\"2000019\" Src=\"2\"/><Pty R=\"22\" ID=\"IFEU\"/><Pty R=\"21\" ID=\"ICEU\"/><Pty R=\"1\" ID=\"GUF\"/><Pty R=\"12\" ID=\"RYN\"/><Pty R=\"4\" ID=\"GUC\"/><Pty R=\"24\" ID=\"CU120978\"/><Pty R=\"38\" ID=\"S\"><Sub ID=\"1\" Typ=\"26\"/></Pty><Amt Typ=\"CRES\"  Amt=\"10.93\" Ccy=\"EUR\"/><Alloc IndAllocID2=\"2827379\" Qty=\"200\"><Pty R=\"22\" ID=\"IFEU\"/> <Pty R=\"21\" ID=\"ICEU\"/> <Pty R=\"1\" ID=\"TUF\"/> <Pty R=\"4\" ID=\"TCF\"/> <Pty R=\"12\" ID=\"TUF\"/> </Alloc> </AllocRpt></FIXML>";

    private static AllocationReport allocationReport;

    private static final String ALLOCATION_REPORT_ID = UUID.randomUUID().toString();

    @BeforeClass
    public static void setUp() throws Exception {
        FixmlMessageParser parser = new FixmlMessageParser();
        allocationReport = parser.parse(String.format(FIXML_ALLOCATION_REPORT_MESSAGE, ALLOCATION_REPORT_ID));
    }

    @Test
    public void parses_allocation_id() throws Exception {
        assertThat(allocationReport.getAllocationId()).isEqualTo(ALLOCATION_REPORT_ID);
    }

    @Test
    public void parses_transaction_type() throws Exception {
        assertThat(allocationReport.getTransactionType()).isEqualTo(TransactionType.NEW);
    }

    @Test
    public void parses_security_id() throws Exception {
        assertThat(allocationReport.getSecurityId()).isEqualTo("2000019");
    }

    @Test
    public void parses_instrument_id_source() throws Exception {
        assertThat(allocationReport.getSecurityIdSource()).isEqualTo(SecurityIDSource.SEDOL);
    }

    @Test
    public void parses_transaction_side() throws Exception {
        assertThat(allocationReport.getTradeSide()).isEqualTo(TradeSide.BUY);
    }

    @Test
    public void parses_quantity() throws Exception {
        assertThat(allocationReport.getQuantity()).isEqualTo(200);
    }

    @Test
    public void parses_allocation_status() throws Exception {
        assertThat(allocationReport.getStatus()).isEqualTo(AllocationStatus.RECEIVED);
    }

    @Test
    public void parses_price() throws Exception {
        assertThat(allocationReport.getPrice()).isEqualTo(BigDecimal.valueOf(57.5054673));
    }

    @Test
    public void parses_trade_date() throws Exception {
        assertThat(allocationReport.getTradeDate()).isEqualTo(
                ZonedDateTime.of(2016, 6, 3, 0, 0, 0, 0, ZoneId.of("GMT"))
        );
    }
}
