package eu.audren.mael.service;

import eu.audren.mael.model.Bill;
import eu.audren.mael.model.SlotType;
import eu.audren.mael.repository.BillRepository;
import eu.audren.mael.repository.domain.BillEntity;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class BillServiceTest {

    @InjectMocks
    private BillService billService;

    @Mock
    private BillRepository billRepository;

    private BillEntity billEntity1;
    private BillEntity billEntity2;
    private BillEntity billEntity3;

    @Before
    public void setUp() {
        billEntity1 = new BillEntity(1L, 10f, "imma1", 1, 100L, 200L, SlotType.STANDARDS);
        billEntity2 = new BillEntity(2L, 25f, "imma2", 1, 100L, 300L, SlotType.ELECTRIC_20KW);
        billEntity3 = new BillEntity(3L, 40f, "imma1", 2, 250, 500L, SlotType.ELECTRIC_50KW);
    }

    @Test
    public void getAllBills() {

        List<BillEntity> bills = Arrays.asList(billEntity1, billEntity2, billEntity3);
        when(billRepository.findAll()).thenReturn(bills);

        List<Bill> billList = billService.retrieveBills(Optional.empty(), Optional.empty());
        assertThat(billList).hasSize(3);
        assertThat(billList.stream().map(Bill::getId).collect(Collectors.toList())).containsExactly(1L, 2L, 3L);
        Bill firstBill = billList.stream().filter(bill -> bill.getId() == 1L).findFirst().get();
        Bill secondBill = billList.stream().filter(bill -> bill.getId() == 2L).findFirst().get();
        Bill thirdBill = billList.stream().filter(bill -> bill.getId() == 3L).findFirst().get();
        assertThatBillHaveTheSameValueThanBillEntity(firstBill, billEntity1);
        assertThatBillHaveTheSameValueThanBillEntity(secondBill, billEntity2);
        assertThatBillHaveTheSameValueThanBillEntity(thirdBill, billEntity3);
    }

    @Test
    public void getPaginatedBillsWithoutPage() {
        Pageable pageable = PageRequest.of(0, 2);
        Page<BillEntity> entityPage = new PageImpl<>(Arrays.asList(billEntity1, billEntity2));
        when(billRepository.findAll(pageable)).thenReturn(entityPage);

        List<Bill> bills = billService.retrieveBills(Optional.empty(), Optional.of(2));
        assertThat(bills.stream().map(Bill::getId).collect(Collectors.toList())).containsExactly(1L, 2L);
    }

    @Test
    public void getPaginatedBillsWithPagination() {
        Pageable pageable = PageRequest.of(1, 2);
        Page<BillEntity> entityPage = new PageImpl<>(Arrays.asList(billEntity3));
        when(billRepository.findAll(pageable)).thenReturn(entityPage);

        List<Bill> bills = billService.retrieveBills(Optional.of(1), Optional.of(2));
        assertThat(bills.stream().map(Bill::getId).collect(Collectors.toList())).containsExactly(3L);
    }

    private void assertThatBillHaveTheSameValueThanBillEntity(Bill bill, BillEntity billEntity) {
        final float tolerance = 0.001f;
        assertThat(bill.getPrice()).isWithin(tolerance).of(billEntity.getPrice());
        assertThat(bill.getCarImmatriculation()).isEqualTo(billEntity.getCarImmatriculation());
        assertThat(bill.getParkingId()).isEqualTo(billEntity.getParkingId());
        assertThat(bill.getArrivalTime()).isEqualTo(billEntity.getArrivalTime());
        assertThat(bill.getDepartureTime()).isEqualTo(billEntity.getDepartureTime());
        assertThat(bill.getSlotType()).isEquivalentAccordingToCompareTo(billEntity.getSlotType());
    }
}
